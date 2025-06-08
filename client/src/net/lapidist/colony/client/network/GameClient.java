package net.lapidist.colony.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.core.serialization.KryoRegistry;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.DispatchListener;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.client.network.handlers.MapStateMessageHandler;
import net.lapidist.colony.client.network.handlers.TileSelectionUpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;


public final class GameClient extends AbstractMessageEndpoint {
    // Increase buffers to handle large serialized map data
    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private final Client client = new Client(BUFFER_SIZE, BUFFER_SIZE);
    private MapState mapState;
    private final Queue<TileSelectionData> tileUpdates = new ConcurrentLinkedQueue<>();
    private Iterable<MessageHandler<?>> handlers;
    private static final int CONNECT_TIMEOUT = 5000;
    private Consumer<MapState> readyCallback;

    public GameClient() {
        this.handlers = java.util.List.of(
                new MapStateMessageHandler(state -> {
                    mapState = state;
                    LOGGER.info("Received map state from server");
                    if (readyCallback != null) {
                        readyCallback.accept(state);
                    }
                }),
                new TileSelectionUpdateHandler(tileUpdates)
        );
    }

    public GameClient(final Iterable<MessageHandler<?>> handlersToUse) {
        this.handlers = handlersToUse;
    }

    private void connect() throws IOException {
        KryoRegistry.register(client.getKryo());
        client.start();
        LOGGER.info("Connecting to server...");

        registerHandlers(handlers);

        client.addListener(new DispatchListener(this::dispatch) {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connected to server");
            }
        });
        client.connect(CONNECT_TIMEOUT, "localhost", GameServer.TCP_PORT, GameServer.UDP_PORT);
    }

    @Override
    public void start() throws IOException, InterruptedException {
        start(ms -> {
        });
    }

    public void start(final Consumer<MapState> callback) throws IOException, InterruptedException {
        this.readyCallback = callback;
        connect();
    }

    public MapState getMapState() {
        return mapState;
    }

    public TileSelectionData pollTileSelectionUpdate() {
        return tileUpdates.poll();
    }

    public void injectTileSelectionUpdate(final TileSelectionData data) {
        tileUpdates.add(data);
    }


    public void sendTileSelectionRequest(final TileSelectionData data) {
        send(data);
    }

    @Override
    public void send(final Object message) {
        client.sendTCP(message);
    }


    @Override
    public void stop() {
        client.stop();
        LOGGER.info("Client stopped");
    }
}
