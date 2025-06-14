package net.lapidist.colony.server.services;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.MapChunkBytes;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.network.DispatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import java.util.function.Consumer;

/**
 * Manages network connections for the game server.
 */
public final class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private final Server server;
    private final int tcpPort;
    private final int udpPort;

    public NetworkService(final Server serverToUse, final int tcp, final int udp) {
        this.server = serverToUse;
        this.tcpPort = tcp;
        this.udpPort = udp;
    }

    /**
     * Starts the server and sets up basic listeners.
     *
     * @param mapState   the current map state to send on connect
     * @param dispatcher dispatcher for incoming messages
     */

    public void start(final MapState mapState, final Consumer<Object> dispatcher) throws IOException {
        server.start();
        LOGGER.info("Server started on TCP {} UDP {}", tcpPort, udpPort);
        server.bind(tcpPort, udpPort);

        server.addListener(new DispatchListener(dispatcher) {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connection established: {}", connection.getID());
                sendMapMetadata(connection, mapState);
            }
        });
    }

    private MapChunkBytes toChunkBytes(
            final int chunkX,
            final int chunkY,
            final MapChunkData chunk
    ) {
        Kryo kryo = server.getKryo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            try (GZIPOutputStream gzip = new GZIPOutputStream(baos); Output output = new Output(gzip)) {
                synchronized (kryo) {
                    kryo.writeObject(output, chunk);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to compress chunk", e);
        }
        return new MapChunkBytes(chunkX, chunkY, baos.toByteArray());
    }

    private void sendMapMetadata(final Connection connection, final MapState state) {
        int chunkCount = state.chunks().size();
        MapMetadata meta = new MapMetadata(
                state.version(),
                state.name(),
                state.saveName(),
                state.autosaveName(),
                state.description(),
                state.buildings(),
                state.playerResources(),
                chunkCount
        );
        connection.sendTCP(meta);
        LOGGER.info("Sent map metadata with {} chunks to connection {}", chunkCount, connection.getID());
    }

    public void sendChunk(final Connection connection, final MapState state, final int chunkX, final int chunkY) {
        MapChunkData chunk = state.getOrCreateChunk(chunkX, chunkY);
        connection.sendTCP(toChunkBytes(chunkX, chunkY, chunk));
    }

    public void stop() {
        server.stop();
    }

    public void broadcast(final Object message) {
        server.sendToAllTCP(message);
    }

    public void broadcastChunk(final MapState state, final int chunkX, final int chunkY) {
        MapChunkData chunk = state.getOrCreateChunk(chunkX, chunkY);
        server.sendToAllTCP(toChunkBytes(chunkX, chunkY, chunk));
    }
}
