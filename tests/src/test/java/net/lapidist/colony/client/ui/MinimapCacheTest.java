package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
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

    private World createWorld() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state);
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
        Texture first = getTexture(cache);
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture second = getTexture(cache);
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
        Texture first = getTexture(cache);
        cache.invalidate();
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture second = getTexture(cache);
        assertNotSame(first, second);
        cache.setViewport(VIEW_SIZE_LARGER, VIEW_SIZE);
        cache.ensureCache(loader, map, mapMapper, tileMapper, SCALE, SCALE);
        Texture third = getTexture(cache);
        assertNotSame(second, third);
    }

    @Test
    public void correctPixelColors() throws Exception {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true).build());
        state.tiles().put(new TilePos(1, 0), TileData.builder()
                .x(1).y(0).tileType("DIRT").passable(true).build());
        World world = new World(new WorldConfigurationBuilder().build());
        MapFactory.create(world, state);
        int mapId = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities().get(0);
        Entity map = world.getEntity(mapId);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);

        MinimapCache cache = new MinimapCache();
        cache.setViewport(VIEW_SIZE, VIEW_SIZE);
        cache.ensureCache(mock(ResourceLoader.class), map, mapMapper, tileMapper, SCALE, SCALE);
        Pixmap pm = getPixmap(cache);
        int y = pm.getHeight() - 1;
        int grass = pm.getPixel(0, y);
        int dirt = pm.getPixel(1, y);
        assertNotEquals(grass, dirt);
    }

    private Texture getTexture(final MinimapCache cache) throws Exception {
        java.lang.reflect.Field f = MinimapCache.class.getDeclaredField("texture");
        f.setAccessible(true);
        return (Texture) f.get(cache);
    }

    private Pixmap getPixmap(final MinimapCache cache) throws Exception {
        java.lang.reflect.Field f = MinimapCache.class.getDeclaredField("pixmap");
        f.setAccessible(true);
        return (Pixmap) f.get(cache);
    }
}
