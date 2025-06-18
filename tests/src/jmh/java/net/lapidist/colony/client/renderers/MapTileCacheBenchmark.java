package net.lapidist.colony.client.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxBenchmarkEnvironment;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;

@State(Scope.Thread)
public class MapTileCacheBenchmark {

    private static final int MAP_SIZE = 120;

    private MapTileCache cache;
    private MapRenderData data;
    private ResourceLoader loader;
    private CameraProvider camera;
    private AssetResolver resolver;
    private com.badlogic.gdx.utils.IntArray updateIndices;
    private MockedConstruction<SpriteCache> construction;

    @Setup(Level.Trial)
    public final void setUp() {
        GdxBenchmarkEnvironment.init();
        loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());
        resolver = new DefaultAssetResolver();
        cache = new MapTileCache();
        camera = createCamera();
        data = createData(MAP_SIZE, MAP_SIZE);
        updateIndices = new com.badlogic.gdx.utils.IntArray(new int[] {0});
        construction = mockConstruction(SpriteCache.class, (mock, ctx) -> {
            when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
            when(mock.endCache()).thenReturn(0);
        });
    }

    @TearDown(Level.Trial)
    public final void tearDown() {
        construction.close();
    }

    private static CameraProvider createCamera() {
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport vp = new ExtendViewport(
                MapState.DEFAULT_WIDTH * GameConstants.TILE_SIZE,
                MapState.DEFAULT_HEIGHT * GameConstants.TILE_SIZE,
                cam
        );
        vp.update((int) vp.getWorldWidth(), (int) vp.getWorldHeight(), true);
        cam.update();
        return new CameraProvider() {
            @Override
            public Camera getCamera() {
                return cam;
            }

            @Override
            public Viewport getViewport() {
                return vp;
            }
        };
    }

    private static MapRenderData createData(final int width, final int height) {
        MapState state = new MapState();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state.tiles().put(new TilePos(x, y), TileData.builder()
                        .x(x).y(y).tileType("GRASS").passable(true)
                        .build());
            }
        }
        World world = new World(new WorldConfigurationBuilder().build());
        Entity map = MapFactory.create(world, state, null);
        ComponentMapper<MapComponent> mapper = world.getMapper(MapComponent.class);
        world.process();
        return MapRenderDataBuilder.fromMap(mapper.get(map), world);
    }

    @Benchmark
    public final void rebuildCache() {
        cache.invalidate();
        cache.ensureCache(loader, data, resolver, camera);
    }

    @Benchmark
    public final void updateTile() {
        cache.invalidateTiles(updateIndices);
        ((net.lapidist.colony.client.render.SimpleMapRenderData) data)
                .setVersion(((net.lapidist.colony.client.render.SimpleMapRenderData) data).getVersion() + 1);
        cache.ensureCache(loader, data, resolver, camera);
    }
}
