package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class RendererEnumMapTest {

    @Test
    public void tileRendererCreatesEntriesForAllEnums() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(anyString())).thenReturn(new TextureRegion());
        TileRenderer renderer = new TileRenderer(
                batch,
                loader,
                mock(CameraProvider.class),
                new DefaultAssetResolver(),
                null
        );

        Field f = TileRenderer.class.getDeclaredField("tileRegions");
        f.setAccessible(true);
        java.util.Map<?, ?> map = (java.util.Map<?, ?>) f.get(renderer);

        String[] types = {"EMPTY", "DIRT", "GRASS"};
        assertEquals(types.length, map.size());
        for (String type : types) {
            assertNotNull(map.get(type));
        }
    }

    @Test
    public void buildingRendererCreatesEntriesForAllEnums() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(anyString())).thenReturn(new TextureRegion());
        BuildingRenderer renderer = new BuildingRenderer(
                batch,
                loader,
                mock(CameraProvider.class),
                new DefaultAssetResolver()
        );

        Field f = BuildingRenderer.class.getDeclaredField("buildingRegions");
        f.setAccessible(true);
        java.util.Map<?, ?> map = (java.util.Map<?, ?>) f.get(renderer);

        String[] buildings = {"HOUSE", "MARKET", "FACTORY", "FARM"};
        assertEquals(buildings.length, map.size());
        for (String type : buildings) {
            assertNotNull(map.get(type));
        }
    }
}
