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

    private static final int LOADER_CALLS = 3;
    private static final float HALF_PROGRESS = 0.5f;

    private static final class DummyBlockingLoader implements ResourceLoader {
        private int calls;
        private boolean loaded;

        @Override
        public void loadTextures(
                final FileLocation location,
                final String path,
                final GraphicsSettings settings
        ) {
            // start load
        }

        @Override
        public void loadTextures(final FileLocation location, final String path) {
            // start load
        }

        @Override
        public boolean isLoaded() {
            return loaded;
        }

        @Override
        public com.badlogic.gdx.graphics.g2d.TextureAtlas getAtlas() {
            return null;
        }

        @Override
        public com.badlogic.gdx.graphics.g2d.TextureRegion findRegion(final String name) {
            return null;
        }

        @Override
        public boolean update() {
            calls++;
            if (calls < LOADER_CALLS) {
                return false;
            }
            loaded = true;
            return true;
        }

        @Override
        public float getProgress() {
            return loaded ? 1f : HALF_PROGRESS;
        }

        @Override
        public void dispose() {
        }
    }

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

    @Test
    public void finishLoadingWaitsUntilReady() {
        DummyBlockingLoader loader = new DummyBlockingLoader();

        loader.finishLoading();

        assertTrue(loader.isLoaded());
        assertEquals(LOADER_CALLS, loader.calls);
    }
}
