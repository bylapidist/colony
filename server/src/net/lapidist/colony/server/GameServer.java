package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.serialization.KryoRegistry;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.handlers.TileSelectionMessageHandler;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.MapService;
import net.lapidist.colony.server.services.NetworkService;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
// Using java.nio here keeps the server independent from LibGDX's FileHandle API
// because the server module runs headless without the Gdx runtime.

public final class GameServer extends AbstractMessageEndpoint implements AutoCloseable {
    public static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    public static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");

    // Increase buffers so the entire map can be serialized in one object
    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final long autosaveInterval;
    private final String saveName;
    private final MapGenerator mapGenerator;
    private AutosaveService autosaveService;
    private MapService mapService;
    private NetworkService networkService;
    private MapState mapState;
    private Iterable<MessageHandler<?>> handlers;

    public GameServer(final GameServerConfig config) {
        this(config, null);
    }

    public GameServer(final GameServerConfig config, final Iterable<MessageHandler<?>> handlersToUse) {
        this.saveName = config.getSaveName();
        this.autosaveInterval = config.getAutosaveInterval();
        this.mapGenerator = config.getMapGenerator();
        this.handlers = handlersToUse;
        this.mapService = new MapService(mapGenerator, saveName);
        this.networkService = new NetworkService(server, TCP_PORT, UDP_PORT);
        this.autosaveService = new AutosaveService(autosaveInterval, saveName, () -> mapState);
    }

    @Override
    public void start() throws IOException, InterruptedException {
        KryoRegistry.register(server.getKryo());
        Events.init(new EventSystem());
        Paths.createGameFoldersIfNotExists();

        mapState = mapService.load();
        networkService.start(mapState, this::dispatch);
        if (handlers == null) {
            handlers = java.util.List.of(
                    new TileSelectionMessageHandler(() -> mapState, server)
            );
        }
        for (MessageHandler<?> handler : handlers) {
            handler.register(this);
        }
        autosaveService.start();
    }

    public MapState getMapState() {
        return mapState;
    }

    @Override
    public void send(final Object message) {
        server.sendToAllTCP(message);
    }


    @Override
    public void stop() {
        autosaveService.stop();
        networkService.stop();
        LOGGER.info("Server stopped");
        Events.dispose();
    }

    @Override
    public void close() {
        stop();
    }
}
