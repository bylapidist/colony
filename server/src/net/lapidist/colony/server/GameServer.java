package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.events.SaveEvent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.serialization.KryoRegistry;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
// Using java.nio here keeps the server independent from LibGDX's FileHandle API
// because the server module runs headless without the Gdx runtime.
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
// Autosave scheduling relies on standard Java concurrency utilities rather than
// any LibGDX specific scheduling.

public final class GameServer implements AutoCloseable {
    public static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    public static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");

    // Increase buffers so the entire map can be serialized in one object
    private static final int BUFFER_SIZE = 65536;
    private static final String AUTOSAVE_SUFFIX = Paths.AUTOSAVE_SUFFIX;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final long autosaveIntervalMs;
    private final String saveName;
    private final MapGenerator mapGenerator;
    private ScheduledExecutorService executor;
    private MapState mapState;

    public GameServer(final GameServerConfig config) {
        this.saveName = config.getSaveName();
        this.autosaveIntervalMs = config.getAutosaveIntervalMs();
        this.mapGenerator = config.getMapGenerator();
    }

    public void start() throws IOException {
        KryoRegistry.register(server.getKryo());
        Events.init(new EventSystem());
        Paths.createGameFoldersIfNotExists();
        Path saveFile = Paths.getAutosave(saveName);

        if (Files.exists(saveFile)) {
            mapState = GameStateIO.load(saveFile);
            LOGGER.info("Loaded save file: {}", saveFile);
            mapState.setSaveName(saveName);
            mapState.setAutosaveName(saveName + AUTOSAVE_SUFFIX);
        } else {
            generateMap();
            mapState.setSaveName(saveName);
            mapState.setAutosaveName(saveName + AUTOSAVE_SUFFIX);
            GameStateIO.save(mapState, saveFile);
            LOGGER.info("Generated new map and saved to: {}", saveFile);
        }
        Files.writeString(Paths.getLastAutosaveMarker(), saveName);

        server.start();
        LOGGER.info("Server started on TCP {} UDP {}", TCP_PORT, UDP_PORT);
        server.bind(TCP_PORT, UDP_PORT);
        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connection established: {}", connection.getID());
                connection.sendTCP(mapState);
                LOGGER.info("Sent map state to connection {}", connection.getID());
            }

            @Override
            public void received(final Connection connection, final Object object) {
                if (object instanceof TileSelectionData) {
                    TileSelectionData data = (TileSelectionData) object;
                    handleTileSelection(data);
                    Events.dispatch(new TileSelectionEvent(data.x(), data.y(), data.selected()));
                    server.sendToAllTCP(data);
                }
            }
        });

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(this::autoSave, autosaveIntervalMs, autosaveIntervalMs, TimeUnit.MILLISECONDS);
    }

    private void generateMap() {
        mapState = mapGenerator.generate(
                GameConstants.MAP_WIDTH,
                GameConstants.MAP_HEIGHT
        );
    }

    public MapState getMapState() {
        return mapState;
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
        saveOnShutdown();
        server.stop();
        LOGGER.info("Server stopped");
        Events.dispose();
    }

    private void handleTileSelection(final TileSelectionData data) {
        for (TileData tile : mapState.getTiles()) {
            if (tile.getX() == data.x() && tile.getY() == data.y()) {
                tile.setSelected(data.selected());
                break;
            }
        }
    }

    private void autoSave() {
        saveGameState(AutosaveEvent::new, "Autosaved game state to {} ({} bytes)");
    }

    private void saveOnShutdown() {
        saveGameState(ShutdownSaveEvent::new, "Saved game state to {} ({} bytes) on shutdown");
    }

    private void saveGameState(final java.util.function.BiFunction<Path, Long, SaveEvent> creator, final String log) {
        try {
            Path file = Paths.getAutosave(saveName);
            GameStateIO.save(mapState, file);
            long size = Files.size(file);
            Events.dispatch(creator.apply(file, size));
            Events.update();
            LOGGER.info(log, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        stop();
    }
}
