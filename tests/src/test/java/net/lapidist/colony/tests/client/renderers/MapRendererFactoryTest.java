package net.lapidist.colony.tests.client.renderers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.SpriteMapRendererFactory;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MapRendererFactoryTest {

    private static final class DummyLoader implements ResourceLoader {
        private boolean loaded;

        @Override
        public void loadTextures(
                final FileLocation location,
                final String path,
                final net.lapidist.colony.settings.GraphicsSettings settings
        ) {
            loaded = true;
        }

        @Override
        public void loadTextures(final FileLocation location, final String path) {
            loaded = true;
        }

        @Override
        public void loadModel(final FileLocation location, final String path) {
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
        public com.badlogic.gdx.graphics.g3d.Model findModel(final String name) {
            return null;
        }

        @Override
        public void dispose() {
        }
    }

    @Test
    public void createLoadsResources() {
        DummyLoader loader = new DummyLoader();
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        try (MockedConstruction<SpriteBatch> ignored =
                mockConstruction(SpriteBatch.class)) {
            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/textures.atlas"
            );
            MapRenderer renderer = factory.create(world);

            assertNotNull(renderer);
            assertTrue(loader.loaded);
        }
        world.dispose();
    }
}
