package net.lapidist.colony.server;

import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.handlers.TileSelectionMessageHandler;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.MapService;
import net.lapidist.colony.server.services.NetworkService;
import net.mostlyoriginal.api.event.common.EventSystem;

import java.io.IOException;
import java.util.List;

/**
 * Coordinates server services such as networking, map management and autosaving.
 */
public final class GameServer extends AbstractMessageEndpoint implements AutoCloseable {
    public static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    public static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");

    private final long autosaveInterval;
    private final String saveName;
    private final MapGenerator mapGenerator;
    private Iterable<MessageHandler<?>> handlers;

    private MapService mapService;
    private NetworkService networkService;
    private AutosaveService autosaveService;

    public GameServer(final GameServerConfig config) {
        this(config, null);
    }

    public GameServer(final GameServerConfig config, final Iterable<MessageHandler<?>> handlersToUse) {
        this.saveName = config.getSaveName();
        this.autosaveInterval = config.getAutosaveInterval();
        this.mapGenerator = config.getMapGenerator();
        this.handlers = handlersToUse;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        Events.init(new EventSystem());

        mapService = new MapService(saveName, mapGenerator);
        mapService.loadOrGenerate();

        networkService = new NetworkService(TCP_PORT, UDP_PORT, mapService::getMapState);
        networkService.setReceiver(this::dispatch);

        if (handlers == null) {
            handlers = List.of(new TileSelectionMessageHandler(mapService::getMapState, networkService.getServer()));
        }
        for (MessageHandler<?> handler : handlers) {
            handler.register(this);
        }

        networkService.start();

        autosaveService = new AutosaveService(autosaveInterval, saveName, mapService::getMapState);
        autosaveService.start();
    }

    public MapState getMapState() {
        return mapService.getMapState();
    }

    @Override
    public void send(final Object message) {
        networkService.send(message);
    }

    @Override
    public void stop() {
        if (autosaveService != null) {
            autosaveService.shutdownNow();
        }
        if (networkService != null) {
            networkService.stop();
        }
        Events.dispose();
    }

    @Override
    public void close() {
        stop();
    }
}
