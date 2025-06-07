package net.lapidist.colony.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.serialization.SerializationRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class GameClient {
    // Increase buffers to handle large serialized map data
    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private final Client client = new Client(BUFFER_SIZE, BUFFER_SIZE);
    private MapState mapState;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int WAIT_TIME_MS = 10;

    public void start() throws IOException, InterruptedException {
        registerClasses();
        client.start();
        LOGGER.info("Connecting to server...");
        client.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connected to server");
            }

            @Override
            public void received(final Connection connection, final Object object) {
                if (object instanceof MapState) {
                    mapState = (MapState) object;
                    LOGGER.info("Received map state from server");
                }
            }
        });
        client.connect(CONNECT_TIMEOUT, "localhost", GameServer.TCP_PORT, GameServer.UDP_PORT);
        while (mapState == null) {
            Thread.sleep(WAIT_TIME_MS);
        }
        LOGGER.info("Map state received, client ready");
    }

    private void registerClasses() {
        SerializationRegistrar.register(client.getKryo());
    }

    public MapState getMapState() {
        return mapState;
    }

    public void sendTileSelection(final TileSelectionData data) {
        client.sendTCP(data);
    }

    public void stop() {
        client.stop();
        LOGGER.info("Client stopped");
    }
}
