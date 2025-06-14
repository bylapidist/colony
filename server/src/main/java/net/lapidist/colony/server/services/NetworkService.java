package net.lapidist.colony.server.services;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.network.DispatchListener;
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
                sendInitialChunks(connection, mapState);
            }
        });
    }

    private MapChunk toChunkMessage(
            final int index,
            final int chunkX,
            final int chunkY,
            final MapChunkData chunk
    ) {
        java.util.Map<TilePos, TileData> tiles = new java.util.HashMap<>(chunk.getTiles().size());
        for (var entry : chunk.getTiles().entrySet()) {
            TileData td = entry.getValue();
            tiles.put(new TilePos(td.x(), td.y()), td);
        }
        return new MapChunk(index, chunkX, chunkY, tiles);
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

    private void sendInitialChunks(final Connection connection, final MapState state) {
        if (state.playerPos() == null) {
            return;
        }
        int chunkX = Math.floorDiv(state.playerPos().x(), MapChunkData.CHUNK_SIZE);
        int chunkY = Math.floorDiv(state.playerPos().y(), MapChunkData.CHUNK_SIZE);
        int radius = GameConstants.CHUNK_LOAD_RADIUS;
        int sent = 0;
        for (int x = chunkX - radius; x <= chunkX + radius; x++) {
            for (int y = chunkY - radius; y <= chunkY + radius; y++) {
                MapChunkData chunk = state.chunks().get(new net.lapidist.colony.components.state.ChunkPos(x, y));
                if (chunk != null) {
                    connection.sendTCP(toChunkMessage(0, x, y, chunk));
                    sent++;
                }
            }
        }
        LOGGER.info("Sent {} initial chunks around spawn to connection {}", sent, connection.getID());
    }

    public void sendChunk(final Connection connection, final MapState state, final int chunkX, final int chunkY) {
        MapChunkData chunk = state.getOrCreateChunk(chunkX, chunkY);
        connection.sendTCP(toChunkMessage(0, chunkX, chunkY, chunk));
    }

    public void stop() {
        server.stop();
    }

    public void broadcast(final Object message) {
        server.sendToAllTCP(message);
    }

    public void broadcastChunk(final MapState state, final int chunkX, final int chunkY) {
        MapChunkData chunk = state.getOrCreateChunk(chunkX, chunkY);
        server.sendToAllTCP(toChunkMessage(0, chunkX, chunkY, chunk));
    }
}
