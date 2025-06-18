package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.tests.GdxBenchmarkEnvironment;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import static org.mockito.Mockito.mock;

/**
 * Benchmarks minimap cache creation for large maps.
 */
@SuppressWarnings("DesignForExtension")
@State(Scope.Thread)
public class MinimapCacheBenchmark {

    private static final int MAP_SIZE = 100;

    private static final float VIEWPORT_SIZE = 64f;

    private World world;
    private Entity map;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ResourceLoader loader;
    private MinimapCache cache;

    @Setup(Level.Trial)
    public final void setUp() {
        GdxBenchmarkEnvironment.init();
        MapState state = new MapState();
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                state.tiles().put(new TilePos(x, y), TileData.builder()
                        .x(x).y(y).tileType("GRASS").passable(true)
                        .build());
            }
        }
        world = new World(new WorldConfigurationBuilder().build());
        map = MapFactory.create(world, state);
        mapMapper = world.getMapper(MapComponent.class);
        tileMapper = world.getMapper(TileComponent.class);
        loader = mock(ResourceLoader.class);
        cache = new MinimapCache();
        cache.setViewport(VIEWPORT_SIZE, VIEWPORT_SIZE);
    }

    @Benchmark
    public final void buildMinimap() {
        cache.invalidate();
        cache.ensureCache(loader, map, mapMapper, tileMapper, 1f, 1f);
    }

    @TearDown(Level.Trial)
    public final void tearDown() {
        cache.dispose();
        world.dispose();
    }
}
