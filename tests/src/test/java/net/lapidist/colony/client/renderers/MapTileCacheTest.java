package net.lapidist.colony.client.renderers;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.systems.CameraProvider;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.components.maps.MapComponent;
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
public class MapTileCacheTest {

    private MapRenderData createData() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        World world = new World(new WorldConfigurationBuilder().build());
        Entity map = MapFactory.create(world, state);
        ComponentMapper<MapComponent> mapMapper = world.getMapper(MapComponent.class);
        world.process();
        return MapRenderDataBuilder.fromMap(mapMapper.get(map), world);
    }

    @Test
    public void doesNotRecreateCacheWhenUnchanged() {
        MapRenderData data = createData();
        CameraProvider cam = mock(CameraProvider.class);
        when(cam.getCamera()).thenReturn(new OrthographicCamera());
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());
        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                    when(mock.endCache()).thenReturn(0);
                })) {
            MapTileCache cache = new MapTileCache();
            cache.ensureCache(loader, data, new DefaultAssetResolver(), cam);
            assertEquals(1, cons.constructed().size());
            cache.ensureCache(loader, data, new DefaultAssetResolver(), cam);
            assertEquals(1, cons.constructed().size());
        }
    }

    @Test
    public void drawRestoresMatrix() {
        MapRenderData data = createData();
        CameraProvider cam = mock(CameraProvider.class);
        when(cam.getCamera()).thenReturn(new OrthographicCamera());
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());
        Matrix4 oldProj = new Matrix4();
        Matrix4 batchProj = new Matrix4();
        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(oldProj);
                    when(mock.endCache()).thenReturn(0);
                })) {
            MapTileCache cache = new MapTileCache();
            cache.ensureCache(loader, data, new DefaultAssetResolver(), cam);
            SpriteCache sprite = cons.constructed().get(0);
            SpriteBatch batch = mock(SpriteBatch.class);
            when(batch.getProjectionMatrix()).thenReturn(batchProj);
            cache.draw(batch);
            InOrder order = inOrder(batch, sprite);
            order.verify(batch).end();
            order.verify(sprite).setProjectionMatrix(batchProj);
            order.verify(sprite).begin();
            order.verify(sprite).draw(anyInt());
            order.verify(sprite).end();
            order.verify(batch).begin();
            order.verify(sprite).setProjectionMatrix(any(Matrix4.class));
        }
    }

    @Test
    public void recreatesCacheWhenVersionChanges() {
        SimpleMapRenderData data = (SimpleMapRenderData) createData();
        CameraProvider cam = mock(CameraProvider.class);
        when(cam.getCamera()).thenReturn(new OrthographicCamera());
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(any())).thenReturn(new TextureRegion());
        try (MockedConstruction<SpriteCache> cons = mockConstruction(SpriteCache.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                    when(mock.endCache()).thenReturn(0);
                })) {
            MapTileCache cache = new MapTileCache();
            cache.ensureCache(loader, data, new DefaultAssetResolver(), cam);
            assertEquals(1, cons.constructed().size());
            data.setVersion(data.getVersion() + 1);
            cache.ensureCache(loader, data, new DefaultAssetResolver(), cam);
            assertEquals(2, cons.constructed().size());
        }
    }
}
