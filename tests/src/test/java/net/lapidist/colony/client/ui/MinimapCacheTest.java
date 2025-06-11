package net.lapidist.colony.client.ui;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MinimapCacheTest {

    private static final float VIEW_SIZE = 64f;
    private static final float SCALE = 1f;
    private static final float DRAW_X = 2f;
    private static final float DRAW_Y = 3f;
    private static final float VIEW_SIZE_LARGER = VIEW_SIZE + 1f;
    private static final int THREE = 3;
    private static final int LARGE_SIZE = 40;

    private World createWorldWithTile() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state);
        return world;
    }

    private World createWorldWithTiles(final int width, final int height) {
        MapState state = new MapState();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state.tiles().put(new TilePos(x, y), TileData.builder()
                        .x(x).y(y).tileType("GRASS").passable(true)
                        .build());
            }
        }
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state);
        return world;
    }

    @Test
    public void doesNotRecreateCacheWhenUnchanged() {
        World world = createWorldWithTile();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());

        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                    when(mock.getTransformMatrix()).thenReturn(new Matrix4());
                    when(mock.endCache()).thenReturn(0);
                });
             MockedConstruction<DefaultAssetResolver> resCons = mockConstruction(DefaultAssetResolver.class,
                (m, c) -> when(m.tileAsset(any())).thenAnswer(inv -> inv.getArgument(0) + "0"))) {
            MinimapCache cache = new MinimapCache();
            cache.setViewport(VIEW_SIZE, VIEW_SIZE);
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
            assertEquals(1, cons.constructed().size());
            verify(loader, times(1)).findRegion(any());

            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
            assertEquals(1, cons.constructed().size());
            verify(loader, times(1)).findRegion(any());
        }
    }

    @Test
    public void recreatesCacheWhenInvalidatedOrViewportChanges() {
        World world = createWorldWithTile();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());

        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                    when(mock.getTransformMatrix()).thenReturn(new Matrix4());
                    when(mock.endCache()).thenReturn(0);
                });
             MockedConstruction<DefaultAssetResolver> resCons = mockConstruction(DefaultAssetResolver.class,
                (m, c) -> when(m.tileAsset(any())).thenAnswer(inv -> inv.getArgument(0) + "0"))) {
            MinimapCache cache = new MinimapCache();
            cache.setViewport(VIEW_SIZE, VIEW_SIZE);
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
            assertEquals(1, cons.constructed().size());

            cache.invalidate();
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
            assertEquals(2, cons.constructed().size());

            cache.setViewport(VIEW_SIZE_LARGER, VIEW_SIZE);
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
            assertEquals(THREE, cons.constructed().size());
        }
    }

    @Test
    public void drawRestoresMatrices() {
        World world = createWorldWithTile();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());

        Matrix4 oldProj = new Matrix4();
        Matrix4 oldTrans = new Matrix4();
        Matrix4 batchProj = new Matrix4();

        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(oldProj);
                    when(mock.getTransformMatrix()).thenReturn(oldTrans);
                    when(mock.endCache()).thenReturn(0);
                });
             MockedConstruction<DefaultAssetResolver> resCons = mockConstruction(DefaultAssetResolver.class,
                (m, c) -> when(m.tileAsset(any())).thenAnswer(inv -> inv.getArgument(0) + "0"))) {
            MinimapCache cache = new MinimapCache();
            cache.setViewport(VIEW_SIZE, VIEW_SIZE);
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);

            SpriteCache sprite = cons.constructed().get(0);
            SpriteBatch batch = mock(SpriteBatch.class);
            when(batch.getProjectionMatrix()).thenReturn(batchProj);

            cache.draw(batch, DRAW_X, DRAW_Y);

            InOrder order = inOrder(batch, sprite);
            order.verify(batch).end();
            order.verify(sprite).setProjectionMatrix(batchProj);
            order.verify(sprite).setTransformMatrix(any(Matrix4.class));
            order.verify(sprite).begin();
            order.verify(sprite).draw(anyInt());
            order.verify(sprite).end();
            order.verify(batch).begin();
            order.verify(sprite).setProjectionMatrix(any(Matrix4.class));
            order.verify(sprite).setTransformMatrix(any(Matrix4.class));
        }
    }

    @Test
    public void createsCacheWithCapacityForLargeMaps() {
        int size = LARGE_SIZE;
        World world = createWorldWithTiles(size, size);
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());

        final java.util.List<?>[] capturedArgs = new java.util.List[1];
        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                    when(mock.getTransformMatrix()).thenReturn(new Matrix4());
                    when(mock.endCache()).thenReturn(0);
                    capturedArgs[0] = ctx.arguments();
                });
             MockedConstruction<DefaultAssetResolver> resCons = mockConstruction(DefaultAssetResolver.class,
                (m, c) -> when(m.tileAsset(any())).thenAnswer(inv -> inv.getArgument(0) + "0"))) {
            MinimapCache cache = new MinimapCache();
            cache.setViewport(VIEW_SIZE, VIEW_SIZE);
            cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);

            assertEquals(Integer.valueOf(size * size), capturedArgs[0].get(0));
            assertEquals(Boolean.TRUE, capturedArgs[0].get(1));
        }
    }
}
