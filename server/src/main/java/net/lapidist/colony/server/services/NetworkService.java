package net.lapidist.colony.server.services;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.ChunkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Manages network connections for the game server.
 */
public final class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private final Server server;
    private final int tcpPort;
    private final int udpPort;
    private final MapService mapService;
    private static final int CHUNK_SIZE = 1024;

    public NetworkService(final Server serverToUse, final int tcp, final int udp, final MapService service) {
        this.server = serverToUse;
        this.tcpPort = tcp;
        this.udpPort = udp;
        this.mapService = service;
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

        server.addListener(new com.esotericsoftware.kryonet.Listener() {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connection established: {}", connection.getID());
                sendMapState(connection, mapState);
            }

            @Override
            public void received(final Connection connection, final Object object) {
                dispatcher.accept(object);
                if (object instanceof ChunkRequest req) {
                    MapChunk chunk = mapService.loadChunk(req.chunkX(), req.chunkY());
                    connection.sendTCP(chunk);
                }
            }
        });
    }

    private void sendMapState(final Connection connection, final MapState state) {
        int totalTiles = state.tiles().size();
        int chunkCount = (int) Math.ceil(totalTiles / (double) CHUNK_SIZE);
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

        if (totalTiles == 0) {
            LOGGER.info("Sent map metadata with no tiles to connection {}", connection.getID());
            return;
        }

        int index = 0;
        java.util.Map<TilePos, TileData> bucket = new java.util.HashMap<>(CHUNK_SIZE);
        for (var entry : state.tiles().entrySet()) {
            bucket.put(entry.getKey(), entry.getValue());
            if (bucket.size() == CHUNK_SIZE) {
                connection.sendTCP(new MapChunk(index++, 0, bucket));
                bucket.clear();
            }
        }
        if (!bucket.isEmpty()) {
            connection.sendTCP(new MapChunk(index, 0, bucket));
            bucket.clear();
        }
        LOGGER.info("Sent map state in {} chunks to connection {}", chunkCount, connection.getID());
    }

    public void stop() {
        server.stop();
    }

    public void broadcast(final Object message) {
        server.sendToAllTCP(message);
    }
}
