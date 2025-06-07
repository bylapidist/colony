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
import net.lapidist.colony.server.events.Events;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.server.io.Paths;
import net.mostlyoriginal.api.event.common.EventSystem;

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
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    // Increase buffers so the entire map can be serialized in one object
    private static final int BUFFER_SIZE = 65536;
    private static final String SAVE_FILE_NAME = "autosave.dat";
    private static final long DEFAULT_INTERVAL_MS = 10 * 60 * 1000L;
    private static final int NAME_RANGE = 100000;

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final long autosaveIntervalMs;
    private ScheduledExecutorService executor;
    private MapState mapState;

    public GameServer() {
        this(DEFAULT_INTERVAL_MS);
    }

    public GameServer(final long autosaveIntervalMsToSet) {
        this.autosaveIntervalMs = autosaveIntervalMsToSet;
    }

    public void start() throws IOException {
        registerClasses();
        Events.init(new EventSystem());
        Paths.createGameFoldersIfNotExists();
        Path saveFile = Paths.getSaveFile(SAVE_FILE_NAME);

        if (Files.exists(saveFile)) {
            mapState = GameStateIO.load(saveFile);
            System.out.printf(
                    "[%s] Loaded save file: %s%n",
                    GameServer.class.getSimpleName(),
                    saveFile
            );
        } else {
            generateMap();
            GameStateIO.save(mapState, saveFile);
            System.out.printf(
                    "[%s] Generated new map and saved to: %s%n",
                    GameServer.class.getSimpleName(),
                    saveFile
            );
        }

        server.start();
        System.out.printf(
                "[%s] Server started on TCP %d UDP %d%n",
                GameServer.class.getSimpleName(),
                TCP_PORT,
                UDP_PORT
        );
        server.bind(TCP_PORT, UDP_PORT);
        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                System.out.printf(
                        "[%s] Connection established: %s%n",
                        GameServer.class.getSimpleName(),
                        connection.getID()
                );
                connection.sendTCP(mapState);
                System.out.printf(
                        "[%s] Sent map state to connection %s%n",
                        GameServer.class.getSimpleName(),
                        connection.getID()
                );
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
        executor.scheduleAtFixedRate(this::autoSave, autosaveIntervalMs, autosaveIntervalMs, TimeUnit.MILLISECONDS);
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
        server.stop();
        System.out.printf(
                "[%s] Server stopped%n",
                GameServer.class.getSimpleName()
        );
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
            System.out.printf(
                    "[%s] Autosaved game state to %s (%d bytes)%n",
                    GameServer.class.getSimpleName(),
                    file,
                    size
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
