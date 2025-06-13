package net.lapidist.colony.server.services;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileData;
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
    private static final int CHUNK_SIZE = MapChunkData.CHUNK_SIZE * MapChunkData.CHUNK_SIZE;

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
                sendMapState(connection, mapState);
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

    private void sendMapState(final Connection connection, final MapState state) {
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

        if (chunkCount == 0) {
            LOGGER.info("Sent map metadata with no tiles to connection {}", connection.getID());
            return;
        }
        int index = 0;
        for (var entry : state.chunks().entrySet()) {
            int chunkX = entry.getKey().x();
            int chunkY = entry.getKey().y();
            connection.sendTCP(toChunkMessage(index++, chunkX, chunkY, entry.getValue()));
        }
        LOGGER.info("Sent map state in {} chunks to connection {}", chunkCount, connection.getID());
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
