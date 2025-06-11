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
        assertTrue(resourceLoader.isLoaded());
    }

    @Test
    public void appliesTextureSettings() throws IOException {
        GraphicsSettings gs = new GraphicsSettings();
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);
        ResourceLoader resourceLoader = new TextureAtlasResourceLoader();

        resourceLoader.loadTextures(FileLocation.INTERNAL, "textures/textures.atlas", gs);

        for (Texture texture : resourceLoader.getAtlas().getTextures()) {
            assertEquals(Texture.TextureFilter.MipMapLinearLinear, texture.getMinFilter());
            assertTrue(texture.getAnisotropicFilter() >= 1f);
        }
    }
}
