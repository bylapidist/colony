package net.lapidist.colony.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.core.serialization.KryoRegistry;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.client.network.handlers.MapStateMessageHandler;
import net.lapidist.colony.client.network.handlers.TileSelectionMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public final class GameClient extends AbstractMessageEndpoint {
    // Increase buffers to handle large serialized map data
    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private final Client client = new Client(BUFFER_SIZE, BUFFER_SIZE);
    private MapState mapState;
    private final Queue<TileSelectionData> tileUpdates = new ConcurrentLinkedQueue<>();
    private Iterable<MessageHandler<?>> handlers;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int WAIT_TIME_MS = 10;

    public GameClient() {
        this.handlers = java.util.List.of(
                new MapStateMessageHandler(state -> {
                    mapState = state;
                    LOGGER.info("Received map state from server");
                }),
                new TileSelectionMessageHandler(tileUpdates)
        );
    }

    public GameClient(final Iterable<MessageHandler<?>> handlersToUse) {
        this.handlers = handlersToUse;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        KryoRegistry.register(client.getKryo());
        client.start();
        LOGGER.info("Connecting to server...");

        for (MessageHandler<?> handler : handlers) {
            handler.register(this);
        }

        client.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connected to server");
            }

            @Override
            public void received(final Connection connection, final Object object) {
                dispatch(object);
            }
        });
        client.connect(CONNECT_TIMEOUT, "localhost", GameServer.TCP_PORT, GameServer.UDP_PORT);
        while (mapState == null) {
            Thread.sleep(WAIT_TIME_MS);
        }
        LOGGER.info("Map state received, client ready");
    }

    public MapState getMapState() {
        return mapState;
    }

    public TileSelectionData pollTileSelection() {
        return tileUpdates.poll();
    }

    public void injectTileSelection(final TileSelectionData data) {
        tileUpdates.add(data);
    }


    public void sendTileSelection(final TileSelectionData data) {
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
