package net.lapidist.colony.client.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.tests.GdxBenchmarkEnvironment;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.lang.reflect.Method;

@State(Scope.Thread)
public class MapRenderDataSystemBenchmark {

    @Param({"30", "60", "90"})
    private int mapSize;

    private World world;
    private MapRenderDataSystem system;
    private ComponentMapper<TileComponent> tileMapper;
    private MapComponent map;
    private Method update;

    @Setup(Level.Trial)
    public final void setUp() throws Exception {
        GdxBenchmarkEnvironment.init();
        MapState state = createState(mapSize, mapSize);
        world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem())
                .build());
        world.process();
        system = world.getSystem(MapRenderDataSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        update = MapRenderDataSystem.class.getDeclaredMethod("updateIncremental");
        update.setAccessible(true);
    }

    @TearDown(Level.Trial)
    public final void tearDown() {
        world.dispose();
    }

    private static MapState createState(final int width, final int height) {
        MapState state = new MapState();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state.tiles().put(new TilePos(x, y), TileData.builder()
                        .x(x).y(y).tileType("GRASS").passable(true)
                        .build());
            }
        }
        return state;
    }

    @Benchmark
    public final void updateIncremental() throws Exception {
        if (map != null && map.getTiles().size > 0) {
            Entity e = map.getTiles().first();
            TileComponent tc = tileMapper.get(e);
            tc.setSelected(!tc.isSelected());
            tc.setDirty(true);
            map.incrementVersion();
        }
        update.invoke(system);
    }
}
