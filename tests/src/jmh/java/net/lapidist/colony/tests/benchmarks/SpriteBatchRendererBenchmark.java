package net.lapidist.colony.tests.benchmarks;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.ResourceRenderer;
import net.lapidist.colony.client.renderers.SpriteBatchMapRenderer;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.client.renderers.PlayerRenderer;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.tests.GdxBenchmarkEnvironment;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;

@State(Scope.Thread)
public class SpriteBatchRendererBenchmark {

    private static final int MAP_SIZE = 30;

    private SpriteBatchMapRenderer cachedRenderer;
    private SpriteBatchMapRenderer plainRenderer;
    private MapRenderData data;
    private CameraProvider camera;
    private ResourceLoader loader;
    private AssetResolver resolver;
    private MockedConstruction<SpriteCache> construction;

    @Setup(Level.Trial)
    public final void setUp() {
        GdxBenchmarkEnvironment.init();
        loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());
        resolver = new DefaultAssetResolver();
        camera = createCamera();
        SpriteBatch batch = mock(SpriteBatch.class);
        TileRenderer tileRenderer = new TileRenderer(batch, loader, camera, resolver, null);
        BuildingRenderer buildingRenderer = new BuildingRenderer(batch, loader, camera, resolver);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        cachedRenderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, true, null);
        plainRenderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, false, null);
        data = createData(MAP_SIZE, MAP_SIZE);
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
        Entity map = MapFactory.create(world, state);
        ComponentMapper<MapComponent> mapper = world.getMapper(MapComponent.class);
        world.process();
        return MapRenderDataBuilder.fromMap(mapper.get(map), world);
    }

    @Benchmark
    public final void renderWithCache() {
        cachedRenderer.render(data, camera);
    }

    @Benchmark
    public final void renderWithoutCache() {
        plainRenderer.render(data, camera);
    }
}
