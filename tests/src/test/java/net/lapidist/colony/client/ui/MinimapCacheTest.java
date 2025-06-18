package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MinimapCacheTest {

    private static final float VIEW_SIZE = 64f;
    private static final float SCALE = 1f;
    private static final float VIEW_SIZE_LARGER = VIEW_SIZE + 1f;
    private static final float DRAW_X = 5f;
    private static final float DRAW_Y = 6f;

    private World createWorld() {
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state, null);
        return world;
    }

    @Test
    public void doesNotRecreateCacheWhenUnchanged() throws Exception {
        World world = createWorld();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);

        MinimapCache cache = new MinimapCache();
        cache.setViewport(VIEW_SIZE, VIEW_SIZE);
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture first = cache.getTexture();
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture second = cache.getTexture();
        assertSame(first, second);
    }

    @Test
    public void recreatesCacheWhenInvalidatedOrViewportChanges() throws Exception {
        World world = createWorld();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        ResourceLoader loader = mock(ResourceLoader.class);

        MinimapCache cache = new MinimapCache();
        cache.setViewport(VIEW_SIZE, VIEW_SIZE);
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture first = cache.getTexture();
        cache.invalidate();
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture second = cache.getTexture();
        assertNotSame(first, second);
        cache.setViewport(VIEW_SIZE_LARGER, VIEW_SIZE);
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture third = cache.getTexture();
        assertNotSame(second, third);
    }

    @Test
    public void correctPixelColors() throws Exception {
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true).build());
        state.putTile(TileData.builder()
                .x(1).y(0).tileType("DIRT").passable(true).build());
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state, null);
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        MinimapCache cache = new MinimapCache();
        cache.setViewport(VIEW_SIZE, VIEW_SIZE);
        cache.ensureCache(mock(ResourceLoader.class), map, mapMapper, tileMapper, SCALE, SCALE);
        Pixmap pm = cache.getPixmap();
        int y = pm.getHeight() - 1;
        int grass = pm.getPixel(0, y);
        int dirt = pm.getPixel(1, y);
        assertNotEquals(grass, dirt);
    }

    @Test
    public void doesNotDrawWithoutTexture() {
        SpriteBatch batch = mock(SpriteBatch.class);
        MinimapCache cache = new MinimapCache();
        cache.draw(batch, DRAW_X, DRAW_Y);
        verifyNoInteractions(batch);
    }

    @Test
    public void drawsTextureWhenAvailable() {
        World world = createWorld();
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);
        MinimapCache cache = new MinimapCache();
        cache.setViewport(VIEW_SIZE, VIEW_SIZE);
        cache.ensureCache(mock(ResourceLoader.class), map, mapMapper, tileMapper, SCALE, SCALE);
        SpriteBatch batch = mock(SpriteBatch.class);
        Texture tex = cache.getTexture();
        cache.draw(batch, DRAW_X, DRAW_Y);
        verify(batch).draw(tex, DRAW_X, DRAW_Y, VIEW_SIZE, VIEW_SIZE);
    }

    private Texture getTexture(final MinimapCache cache) {
        return cache.getTexture();
    }

    private Pixmap getPixmap(final MinimapCache cache) {
        return cache.getPixmap();
    }
}
