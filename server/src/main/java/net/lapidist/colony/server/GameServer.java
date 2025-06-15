package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.base.ModMetadataUtil;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.handlers.TileSelectionRequestHandler;
import net.lapidist.colony.server.handlers.BuildingPlacementRequestHandler;
import net.lapidist.colony.server.handlers.BuildingRemovalRequestHandler;
import net.lapidist.colony.server.handlers.ChatMessageHandler;
import net.lapidist.colony.server.handlers.ResourceGatherRequestHandler;
import net.lapidist.colony.server.handlers.MapChunkRequestHandler;
import net.lapidist.colony.server.handlers.PlayerPositionUpdateHandler;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.CommandHandler;
import net.lapidist.colony.server.commands.TileSelectionCommandHandler;
import net.lapidist.colony.server.commands.BuildCommandHandler;
import net.lapidist.colony.server.commands.GatherCommandHandler;
import net.lapidist.colony.server.commands.RemoveBuildingCommandHandler;
import net.lapidist.colony.server.commands.PlayerPositionCommandHandler;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.MapService;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.ResourceProductionService;
import net.lapidist.colony.mod.GameSystem;
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
public final class GameServer extends AbstractMessageEndpoint implements AutoCloseable,
        net.lapidist.colony.mod.GameServer {

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
    private java.util.function.Supplier<MapService> mapServiceFactory;
    private java.util.function.Supplier<NetworkService> networkServiceFactory;
    private java.util.function.Supplier<AutosaveService> autosaveServiceFactory;
    private java.util.function.Supplier<ResourceProductionService> resourceProductionServiceFactory;
    private java.util.function.Supplier<CommandBus> commandBusFactory;
    private MapState mapState;
    private Iterable<MessageHandler<?>> handlers;
    private Iterable<CommandHandler<?>> commandHandlers;
    private java.util.List<LoadedMod> mods;
    private final java.util.List<GameSystem> systems = new java.util.ArrayList<>();

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

        loadMapState();

        if (mods == null) {
            mods = new java.util.ArrayList<>();
            for (GameMod builtin : java.util.ServiceLoader.load(GameMod.class)) {
                ModMetadata meta = ModMetadataUtil.builtinMetadata(builtin.getClass());
                mods.add(new LoadedMod(builtin, meta));
            }
            mods.addAll(new ModLoader(Paths.get()).loadMods());
        }

        for (LoadedMod mod : mods) {
            mod.mod().init();
        }

        for (LoadedMod mod : mods) {
            mod.mod().registerServices(this);
        }

        // re-create services after mods potentially changed the factories
        if (mapServiceFactory == null) {
            mapServiceFactory = () -> new MapService(
                    mapGenerator,
                    saveName,
                    mapWidth,
                    mapHeight,
                    stateLock
            );
        }
        this.mapService = mapServiceFactory.get();
        this.networkService = networkServiceFactory.get();
        this.autosaveService = autosaveServiceFactory.get();
        this.resourceProductionService = resourceProductionServiceFactory.get();
        this.commandBus = commandBusFactory.get();

        for (LoadedMod mod : mods) {
            mod.mod().registerSystems(this);
        }

        if (mapState == null) {
            loadMapState();
        }
        startNetwork();

        for (LoadedMod mod : mods) {
            mod.mod().registerHandlers(commandBus);
        }

        autosaveService.start();
        for (GameSystem system : systems) {
            system.start();
        }
    }

    private void initKryo() {
        KryoRegistry.register(server.getKryo());
    }

    private void loadMapState() throws IOException {
        Paths.get().createGameFoldersIfNotExists();
        java.nio.file.Path file = Paths.get().getAutosave(saveName);
        if (java.nio.file.Files.exists(file)) {
            net.lapidist.colony.save.SaveData data = GameStateIO.loadData(file);
            mapState = data.mapState();
            if (mods == null) {
                mods = new java.util.ArrayList<>();
                boolean useAll = data.mods().isEmpty();
                for (GameMod builtin : java.util.ServiceLoader.load(GameMod.class)) {
                    ModMetadata meta = ModMetadataUtil.builtinMetadata(builtin.getClass());
                    if (useAll || data.mods().stream().anyMatch(m -> m.id().equals(meta.id()))) {
                        mods.add(new LoadedMod(builtin, meta));
                    }
                }
                java.util.List<LoadedMod> loaded = new ModLoader(Paths.get()).loadMods();
                for (LoadedMod mod : loaded) {
                    if (useAll || data.mods().stream().anyMatch(m -> m.id().equals(mod.metadata().id()))) {
                        mods.add(mod);
                    }
                }
            }
        } else if (mapService != null) {
            mapState = mapService.load();
        }
    }

    private void startNetwork() throws IOException {
        networkService.start(mapState, this::dispatch);
    }

    /** Register built-in command and network handlers. */
    public void registerDefaultHandlers() {
        if (commandHandlers == null) {
            commandHandlers = java.util.List.of(
                    new TileSelectionCommandHandler(() -> mapState, networkService, stateLock),
                    new BuildCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock),
                    new GatherCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock),
                    new RemoveBuildingCommandHandler(() -> mapState, s -> mapState = s, networkService, stateLock),
                    new PlayerPositionCommandHandler(() -> mapState, s -> mapState = s, stateLock)
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
                    new MapChunkRequestHandler(() -> mapState, networkService, stateLock),
                    new PlayerPositionUpdateHandler(commandBus)
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

    /** Replace the current map state with a new instance. */
    public void setMapState(final MapState newState) {
        stateLock.lock();
        try {
            this.mapState = newState;
        } finally {
            stateLock.unlock();
        }
    }

    /** Access to the default resource production service instance. */
    public ResourceProductionService getResourceProductionService() {
        return resourceProductionService;
    }

    /**
     * Register a system to start and stop with the server lifecycle.
     * Duplicate registrations are ignored to prevent multiple start calls.
     */
    @Override
    public void registerSystem(final GameSystem system) {
        if (!systems.contains(system)) {
            systems.add(system);
        }
    }

    /**
     * Indicates if the server network thread is currently running.
     */
    public boolean isRunning() {
        Thread t = server.getUpdateThread();
        return t != null && t.isAlive();
    }

    /** Access to the save name used by this server. */
    public String getSaveName() {
        return saveName;
    }

    /** Interval between autosaves in milliseconds. */
    public long getAutosaveInterval() {
        return autosaveInterval;
    }

    /** Map generator configured for this server. */
    public MapGenerator getMapGenerator() {
        return mapGenerator;
    }

    /** Configured map width in tiles. */
    public int getMapWidth() {
        return mapWidth;
    }

    /** Configured map height in tiles. */
    public int getMapHeight() {
        return mapHeight;
    }

    /** Lock guarding access to the map state. */
    public ReentrantLock getStateLock() {
        return stateLock;
    }

    /** Underlying KryoNet server instance. */
    public Server getServer() {
        return server;
    }

    /** Network service used by the server. */
    public NetworkService getNetworkService() {
        return networkService;
    }

    /**
     * Override the factory for creating {@link MapService} instances.
     * Modifications must occur before calling {@link #start()}.
     */
    public void setMapServiceFactory(final java.util.function.Supplier<MapService> factory) {
        this.mapServiceFactory = factory;
    }

    /**
     * Current factory used to create {@link MapService} instances.
     */
    public java.util.function.Supplier<MapService> getMapServiceFactory() {
        return mapServiceFactory;
    }

    /**
     * Override the factory for creating {@link NetworkService} instances.
     * Modifications must occur before calling {@link #start()}.
     */
    public void setNetworkServiceFactory(
            final java.util.function.Supplier<NetworkService> factory) {
        this.networkServiceFactory = factory;
    }

    /**
     * Current factory used to create {@link NetworkService} instances.
     */
    public java.util.function.Supplier<NetworkService> getNetworkServiceFactory() {
        return networkServiceFactory;
    }

    /**
     * Override the factory for creating {@link AutosaveService} instances.
     * Modifications must occur before calling {@link #start()}.
     */
    public void setAutosaveServiceFactory(
            final java.util.function.Supplier<AutosaveService> factory) {
        this.autosaveServiceFactory = factory;
    }

    /**
     * Current factory used to create {@link AutosaveService} instances.
     */
    public java.util.function.Supplier<AutosaveService> getAutosaveServiceFactory() {
        return autosaveServiceFactory;
    }

    /**
     * Override the factory for creating {@link ResourceProductionService} instances.
     * Modifications must occur before calling {@link #start()}.
     */
    public void setResourceProductionServiceFactory(
            final java.util.function.Supplier<ResourceProductionService> factory) {
        this.resourceProductionServiceFactory = factory;
    }

    /**
     * Current factory used to create {@link ResourceProductionService} instances.
     */
    public java.util.function.Supplier<ResourceProductionService> getResourceProductionServiceFactory() {
        return resourceProductionServiceFactory;
    }

    /**
     * Override the factory for creating {@link CommandBus} instances.
     * Modifications must occur before calling {@link #start()}.
     */
    public void setCommandBusFactory(
            final java.util.function.Supplier<CommandBus> factory) {
        this.commandBusFactory = factory;
    }

    /**
     * Current factory used to create {@link CommandBus} instances.
     */
    public java.util.function.Supplier<CommandBus> getCommandBusFactory() {
        return commandBusFactory;
    }

    /**
     * Specify the list of mods to load. Must be called before {@link #start()}.
     */
    public void setMods(final java.util.List<LoadedMod> modsToUse) {
        this.mods = modsToUse;
    }

    /**
     * Metadata for all currently loaded mods.
     */
    public java.util.List<ModMetadata> getModMetadata() {
        if (mods == null) {
            return java.util.List.of();
        }
        return mods.stream().map(LoadedMod::metadata).toList();
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
        for (GameSystem system : systems) {
            system.stop();
        }
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
