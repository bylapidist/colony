package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.events.Events;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.server.io.Paths;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
// Using java.nio here keeps the server independent from LibGDX's FileHandle API
// because the server module runs headless without the Gdx runtime.
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
// Autosave scheduling relies on standard Java concurrency utilities rather than
// any LibGDX specific scheduling.

public final class GameServer {

    // Increase buffers so the entire map can be serialized in one object
    private static final int BUFFER_SIZE = 65536;
    private static final String SAVE_FILE_NAME = "autosave.dat";
    private static final int NAME_RANGE = 100000;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final ServerConfig config;
    private ScheduledExecutorService executor;
    private MapState mapState;

    public GameServer() {
        this(ServerConfig.load());
    }

    public GameServer(final long autosaveIntervalMsToSet) {
        this(new ServerConfig(ServerConfig.DEFAULT_TCP_PORT, ServerConfig.DEFAULT_UDP_PORT, autosaveIntervalMsToSet));
    }

    public GameServer(final ServerConfig configToSet) {
        this.config = configToSet;
    }

    public void start() throws IOException {
        registerClasses();
        Events.init(new EventSystem());
        Paths.createGameFoldersIfNotExists();
        Path saveFile = Paths.getSaveFile(SAVE_FILE_NAME);

        if (Files.exists(saveFile)) {
            mapState = GameStateIO.load(saveFile);
            LOGGER.info("Loaded save file: {}", saveFile);
        } else {
            generateMap();
            GameStateIO.save(mapState, saveFile);
            LOGGER.info("Generated new map and saved to: {}", saveFile);
        }

        server.start();
        LOGGER.info(
                "Server started on TCP {} UDP {}",
                config.getTcpPort(),
                config.getUdpPort()
        );
        server.bind(config.getTcpPort(), config.getUdpPort());
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
                    Events.dispatch(new TileSelectionEvent(data.getX(), data.getY(), data.isSelected()));
                }
            }
        });

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(
                this::autoSave,
                config.getAutosaveIntervalMs(),
                config.getAutosaveIntervalMs(),
                TimeUnit.MILLISECONDS
        );
    }

    private void registerClasses() {
        server.getKryo().register(MapState.class);
        server.getKryo().register(TileData.class);
        server.getKryo().register(BuildingData.class);
        server.getKryo().register(TileSelectionData.class);
        server.getKryo().register(java.util.ArrayList.class);
        server.getKryo().register(java.util.List.class);
    }

    private void generateMap() {
        mapState = new MapState();
        Random random = new Random();
        mapState.setName("map-" + random.nextInt(NAME_RANGE));
        mapState.setDescription("Generated map");
        String[] textures = new String[]{"grass0", "dirt0"};
        for (int x = 0; x <= GameConstants.MAP_WIDTH; x++) {
            for (int y = 0; y <= GameConstants.MAP_HEIGHT; y++) {
                TileData tile = new TileData();
                tile.setX(x);
                tile.setY(y);
                tile.setTileType("GRASS");
                tile.setTextureRef(textures[random.nextInt(textures.length)]);
                tile.setPassable(true);
                tile.setSelected(false);
                mapState.getTiles().add(tile);
            }
        }
        BuildingData building = new BuildingData();
        building.setX(1);
        building.setY(1);
        building.setBuildingType("HOUSE");
        building.setTextureRef("house0");
        mapState.getBuildings().add(building);
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
            if (tile.getX() == data.getX() && tile.getY() == data.getY()) {
                tile.setSelected(data.isSelected());
                break;
            }
        }
    }

    private void autoSave() {
        try {
            Path file = Paths.getSaveFile(SAVE_FILE_NAME);
            GameStateIO.save(mapState, file);
            long size = Files.size(file);
            Events.dispatch(new AutosaveEvent(file, size));
            Events.update();
            LOGGER.info("Autosaved game state to {} ({} bytes)", file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOnShutdown() {
        try {
            Path file = Paths.getSaveFile(SAVE_FILE_NAME);
            GameStateIO.save(mapState, file);
            long size = Files.size(file);
            Events.dispatch(new ShutdownSaveEvent(file, size));
            Events.update();
            LOGGER.info("Saved game state to {} ({} bytes) on shutdown", file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
