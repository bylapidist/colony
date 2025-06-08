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
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.CommandHandler;
import net.lapidist.colony.server.commands.TileSelectionCommandHandler;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.MapService;
import net.lapidist.colony.server.services.NetworkService;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class GameServer extends AbstractMessageEndpoint implements AutoCloseable {
    public static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    public static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");

    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final long autosaveInterval;
    private final String saveName;
    private final MapGenerator mapGenerator;
    private final MapService mapService;
    private final NetworkService networkService;
    private final AutosaveService autosaveService;
    private final CommandBus commandBus;
    private Iterable<MessageHandler<?>> handlers;
    private Iterable<CommandHandler<?>> commandHandlers;
    private MapState mapState;

    public GameServer(final GameServerConfig config) {
        this(config, null, null);
    }

    public GameServer(final GameServerConfig config,
                      final Iterable<MessageHandler<?>> handlersToUse) {
        this(config, handlersToUse, null);
    }

    public GameServer(final GameServerConfig config,
                      final Iterable<MessageHandler<?>> handlersToUse,
                      final Iterable<CommandHandler<?>> commandHandlersToUse) {
        this.saveName = config.getSaveName();
        this.autosaveInterval = config.getAutosaveInterval();
        this.mapGenerator = config.getMapGenerator();
        this.handlers = handlersToUse;
        this.commandHandlers = commandHandlersToUse;
        this.mapService = new MapService(mapGenerator, saveName);
        this.networkService = new NetworkService(server, TCP_PORT, UDP_PORT);
        this.autosaveService = new AutosaveService(autosaveInterval, saveName, () -> mapState);
        this.commandBus = new CommandBus();
    }

    @Override
    public void start() throws IOException, InterruptedException {
        KryoRegistry.register(server.getKryo());
        Events.init(new EventSystem());
        Paths.createGameFoldersIfNotExists();

        mapState = mapService.load();
        networkService.start(mapState, this::dispatch);

        if (commandHandlers == null) {
            commandHandlers = java.util.List.of(
                    new TileSelectionCommandHandler(() -> mapState, networkService)
            );
        }
        commandBus.registerHandlers(commandHandlers);

        if (handlers == null) {
            handlers = java.util.List.of(
                    new TileSelectionMessageHandler(commandBus)
            );
        }
        registerHandlers(handlers);
        autosaveService.start();
    }

    public MapState getMapState() {
        return mapState;
    }

    @Override
    public void send(final Object message) {
        networkService.send(message);
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
