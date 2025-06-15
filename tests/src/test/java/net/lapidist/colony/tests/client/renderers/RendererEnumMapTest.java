package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.registry.Registries;
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
        new BaseDefinitionsMod().init();
        GraphicsSettings gs = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(
                batch,
                loader,
                mock(CameraProvider.class),
                new DefaultAssetResolver(),
                null,
                gs
        );

        Field f = TileRenderer.class.getDeclaredField("tileRegions");
        f.setAccessible(true);
        java.util.Map<?, ?> map = (java.util.Map<?, ?>) f.get(renderer);

        int expected = Registries.tiles().all().size();
        assertEquals(expected, map.size());
        for (var def : Registries.tiles().all()) {
            assertNotNull(map.get(def.id().toUpperCase(java.util.Locale.ROOT)));
        }
    }

    @Test
    public void buildingRendererCreatesEntriesForAllEnums() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.findRegion(anyString())).thenReturn(new TextureRegion());
        new BaseDefinitionsMod().init();
        GraphicsSettings gs = new GraphicsSettings();
        BuildingRenderer renderer = new BuildingRenderer(
                batch,
                loader,
                mock(CameraProvider.class),
                new DefaultAssetResolver(),
                gs
        );

        Field f = BuildingRenderer.class.getDeclaredField("buildingRegions");
        f.setAccessible(true);
        java.util.Map<?, ?> map = (java.util.Map<?, ?>) f.get(renderer);

        int expected = Registries.buildings().all().size();
        assertEquals(expected, map.size());
        for (var def : Registries.buildings().all()) {
            assertNotNull(map.get(def.id().toUpperCase(java.util.Locale.ROOT)));
        }
    }
}
