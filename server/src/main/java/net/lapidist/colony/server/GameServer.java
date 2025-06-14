package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.handlers.TileSelectionRequestHandler;
import net.lapidist.colony.server.handlers.BuildingPlacementRequestHandler;
import net.lapidist.colony.server.handlers.BuildingRemovalRequestHandler;
import net.lapidist.colony.server.handlers.ChatMessageHandler;
import net.lapidist.colony.server.handlers.ResourceGatherRequestHandler;
import net.lapidist.colony.server.handlers.MapChunkRequestHandler;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.CommandHandler;
import net.lapidist.colony.server.commands.TileSelectionCommandHandler;
import net.lapidist.colony.server.commands.BuildCommandHandler;
import net.lapidist.colony.server.commands.GatherCommandHandler;
import net.lapidist.colony.server.commands.RemoveBuildingCommandHandler;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.MapService;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.ResourceProductionService;
import java.util.concurrent.locks.ReentrantLock;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
// Using java.nio here keeps the server independent from LibGDX's FileHandle API
// because the server module runs headless without the Gdx runtime.

/**
 * Main entry point for the dedicated game server. All access to {@code mapState} must
 * be guarded by {@link #stateLock}. Services and command handlers receive the same
 * lock instance and are responsible for locking when reading or modifying state.
 */
public final class GameServer extends AbstractMessageEndpoint implements AutoCloseable {
    public static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    public static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");

    // Buffer size for Kryo serialization configured via game.networkBufferSize.
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final Server server = new Server(GameConstants.NETWORK_BUFFER_SIZE, GameConstants.NETWORK_BUFFER_SIZE);
    private final long autosaveInterval;
    private final String saveName;
    private final MapGenerator mapGenerator;
    private final int mapWidth;
    private final int mapHeight;
    private final ReentrantLock stateLock = new ReentrantLock();
    private AutosaveService autosaveService;
    private ResourceProductionService resourceProductionService;
    private MapService mapService;
    private NetworkService networkService;
    private CommandBus commandBus;
    private MapState mapState;
    private Iterable<MessageHandler<?>> handlers;
    private Iterable<CommandHandler<?>> commandHandlers;
    private java.util.List<LoadedMod> mods;

    /**
     * Create a new server instance using the supplied configuration.
     *
     * @param config configuration for map generation and runtime settings
     */
    public GameServer(final GameServerConfig config) {
        this(config, null, null);
    }

    /**
     * Create a new server with custom network message handlers.
     *
     * @param config        game configuration
     * @param handlersToUse handlers to register for incoming messages
     */
    public GameServer(final GameServerConfig config,
                      final Iterable<MessageHandler<?>> handlersToUse) {
        this(config, handlersToUse, null);
    }

    /**
     * Create a fully customised server instance.
     *
     * @param config              game configuration
     * @param handlersToUse       handlers for network messages
     * @param commandHandlersToUse command handlers for in-game commands
     */
    public GameServer(final GameServerConfig config,
                      final Iterable<MessageHandler<?>> handlersToUse,
                      final Iterable<CommandHandler<?>> commandHandlersToUse) {
        this.saveName = config.getSaveName();
        this.autosaveInterval = config.getAutosaveInterval();
        this.mapGenerator = config.getMapGenerator();
        this.mapWidth = config.getWidth();
        this.mapHeight = config.getHeight();
        this.handlers = handlersToUse;
        this.commandHandlers = commandHandlersToUse;
        this.mapService = new MapService(mapGenerator, saveName, mapWidth, mapHeight, stateLock);
        this.networkService = new NetworkService(server, TCP_PORT, UDP_PORT);
        this.autosaveService = new AutosaveService(autosaveInterval, saveName, () -> mapState, stateLock);
        this.resourceProductionService = new ResourceProductionService(
                autosaveInterval,
                () -> mapState,
                s -> mapState = s,
                networkService,
                stateLock
        );
        this.commandBus = new CommandBus();
    }

    @Override
    /**
     * Start all server subsystems and begin accepting client connections.
     *
     * @throws IOException          if map data cannot be loaded or networking fails
     * @throws InterruptedException if the network thread is interrupted
     */
    public void start() throws IOException, InterruptedException {
        initKryo();
        Events.init(new EventSystem());
        mods = new ModLoader(Paths.get()).loadMods();
        for (LoadedMod mod : mods) {
            mod.mod().init();
        }
        loadMapState();
        startNetwork();
        registerDefaultHandlers();
        autosaveService.start();
        resourceProductionService.start();
    }

    private void initKryo() {
        KryoRegistry.register(server.getKryo());
    }

    private void loadMapState() throws IOException {
        Paths.get().createGameFoldersIfNotExists();
        mapState = mapService.load();
    }

    private void startNetwork() throws IOException {
        networkService.start(mapState, this::dispatch);
    }

    private void registerDefaultHandlers() {
        if (commandHandlers == null) {
            commandHandlers = java.util.List.of(
                    new TileSelectionCommandHandler(() -> mapState, networkService, stateLock),
                    new BuildCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock),
                    new GatherCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock),
                    new RemoveBuildingCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock)
            );
        }
        commandBus.registerHandlers(commandHandlers);

        if (handlers == null) {
            handlers = java.util.List.of(
                    new TileSelectionRequestHandler(commandBus),
                    new BuildingPlacementRequestHandler(commandBus),
                    new BuildingRemovalRequestHandler(commandBus),
                    new ChatMessageHandler(networkService, commandBus),
                    new ResourceGatherRequestHandler(commandBus),
                    new MapChunkRequestHandler(() -> mapState, networkService, stateLock)
            );
        }
        registerHandlers(handlers);
    }

    /**
     * Current authoritative map state held by the server.
     *
     * @return loaded map state
     */
    public MapState getMapState() {
        stateLock.lock();
        try {
            return mapState;
        } finally {
            stateLock.unlock();
        }
    }

    /**
     * Indicates if the server network thread is currently running.
     */
    public boolean isRunning() {
        Thread t = server.getUpdateThread();
        return t != null && t.isAlive();
    }

    /**
     * Broadcast a message to all connected clients.
     *
     * @param message message to send
     */
    public void broadcast(final Object message) {
        networkService.broadcast(message);
    }

    @Override
    /**
     * Send a message to all clients. Convenience method delegating to {@link #broadcast(Object)}.
     */
    public void send(final Object message) {
        broadcast(message);
    }


    @Override
    /**
     * Stop all server services and dispose loaded mods.
     */
    public void stop() {
        resourceProductionService.stop();
        autosaveService.stop();
        networkService.stop();
        if (mods != null) {
            for (LoadedMod mod : mods) {
                mod.mod().dispose();
            }
        }
        LOGGER.info("Server stopped");
        Events.dispose();
    }

    @Override
    /**
     * Close the server, alias for {@link #stop()}.
     */
    public void close() {
        stop();
    }
}
