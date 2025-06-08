package net.lapidist.colony.server.services;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.core.serialization.KryoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Sets up the network server and forwards incoming messages.
 */
public final class NetworkService {
    private static final int BUFFER_SIZE = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private final int tcpPort;
    private final int udpPort;
    private final Supplier<MapState> stateSupplier;

    private Consumer<Object> receiver;

    public NetworkService(
            final int tcpPortToUse,
            final int udpPortToUse,
            final Supplier<MapState> stateSupplierToUse
    ) {
        this.tcpPort = tcpPortToUse;
        this.udpPort = udpPortToUse;
        this.stateSupplier = stateSupplierToUse;
    }

    public void setReceiver(final Consumer<Object> receiverToUse) {
        this.receiver = receiverToUse;
    }

    public void start() throws IOException {
        KryoRegistry.register(server.getKryo());
        server.start();
        LOGGER.info("Server started on TCP {} UDP {}", tcpPort, udpPort);
        server.bind(tcpPort, udpPort);

        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connection established: {}", connection.getID());
                connection.sendTCP(stateSupplier.get());
                LOGGER.info("Sent map state to connection {}", connection.getID());
            }

            @Override
            public void received(final Connection connection, final Object object) {
                if (receiver != null) {
                    receiver.accept(object);
                }
            }
        });
    }

    public void send(final Object message) {
        server.sendToAllTCP(message);
    }

    public void stop() {
        server.stop();
        LOGGER.info("Server stopped");
    }

    public Server getServer() {
        return server;
    }
}
