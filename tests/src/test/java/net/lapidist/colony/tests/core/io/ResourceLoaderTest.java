package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.settings.GraphicsSettings;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ResourceLoaderTest {

    @Test
    public final void testLoadsResources() throws IOException {
        ResourceLoader resourceLoader = new TextureAtlasResourceLoader();

        GraphicsSettings gs = new GraphicsSettings();
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);

        resourceLoader.loadTextures(
                FileLocation.INTERNAL,
                "textures/textures.atlas",
                gs
        );
        while (!resourceLoader.update()) {
            assertTrue(resourceLoader.getProgress() < 1f);
        }
        assertTrue(resourceLoader.isLoaded());
    }

    @Test
    public void appliesTextureSettings() throws IOException {
        GraphicsSettings gs = new GraphicsSettings();
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);
        ResourceLoader resourceLoader = new TextureAtlasResourceLoader();

        resourceLoader.loadTextures(FileLocation.INTERNAL, "textures/textures.atlas", gs);
        resourceLoader.finishLoading();

        for (Texture texture : resourceLoader.getAtlas().getTextures()) {
            assertEquals(Texture.TextureFilter.MipMapLinearLinear, texture.getMinFilter());
            assertTrue(texture.getAnisotropicFilter() >= 1f);
            assertTrue(texture.getTextureData().useMipMaps());
        }
    }

    @Test
    public void updateReturnsFalseWhenNotStarted() {
        ResourceLoader loader = new TextureAtlasResourceLoader();

        assertFalse(loader.update());
        assertEquals(0f, loader.getProgress(), 0f);
        assertFalse(loader.isLoaded());
        assertNull(loader.findRegion("missing"));
    }
}
