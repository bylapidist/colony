package net.lapidist.colony.server.benchmarks;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.components.state.map.ChunkPos;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@State(Scope.Thread)
public class NetworkServiceBenchmark {

    private static final int MAP_SIZE = 64;

    private NetworkService service;
    private Connection connection;
    private MapState state;
    private Method sendMapMetadata;
    private Method sendChunk;

    @Setup(Level.Trial)
    public final void setUp() throws Exception {
        Server server = mock(Server.class);
        service = new NetworkService(server, 1, 2);
        connection = mock(Connection.class);
        state = createState(MAP_SIZE, MAP_SIZE);
        sendMapMetadata = NetworkService.class.getDeclaredMethod("sendMapMetadata", Connection.class, MapState.class);
        sendMapMetadata.setAccessible(true);
        sendChunk = NetworkService.class.getDeclaredMethod(
                "sendChunk",
                Connection.class,
                MapState.class,
                int.class,
                int.class
        );
        sendChunk.setAccessible(true);
    }

    private static MapState createState(final int width, final int height) {
        MapState s = new MapState();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                s.tiles().put(new TilePos(x, y), TileData.builder()
                        .x(x).y(y).tileType("GRASS").passable(true)
                        .build());
            }
        }
        return s;
    }

    @Benchmark
    public final void sendMapState() throws Exception {
        sendMapMetadata.invoke(service, connection, state);
        for (var entry : state.chunks().entrySet()) {
            ChunkPos pos = entry.getKey();
            sendChunk.invoke(service, connection, state, pos.x(), pos.y());
        }
    }
}
