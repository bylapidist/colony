package net.lapidist.colony.tests.client.renderers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.ModelBatchMapRendererFactory;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ModelBatchMapRendererFactoryTest {

    private static final class DummyLoader implements ResourceLoader {
        private boolean loaded;
        private boolean updated;

        @Override
        public void loadTextures(
                final FileLocation location,
                final String path,
                final net.lapidist.colony.settings.GraphicsSettings settings
        ) {
            // start load
        }

        @Override
        public void loadTextures(final FileLocation location, final String path) {
            // start load
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
        public boolean update() {
            updated = true;
            loaded = true;
            return true;
        }

        @Override
        public float getProgress() {
            return 1f;
        }

        @Override
        public void dispose() {
        }
    }

    @Test
    public void createsRendererWithResources() {
        World world = new World(new WorldConfigurationBuilder().build());
        try (MockedConstruction<ModelBatch> ignored = mockConstruction(ModelBatch.class)) {
            ModelBatchMapRendererFactory factory = new ModelBatchMapRendererFactory();
            MapRenderer renderer = factory.create(world);
            assertNotNull(renderer);
        }
        world.dispose();
    }

    @Test
    public void createsRendererWithProgressCallback() {
        DummyLoader loader = new DummyLoader();
        World world = new World(new WorldConfigurationBuilder().build());
        try (MockedConstruction<ModelBatch> ignored = mockConstruction(ModelBatch.class)) {
            java.util.concurrent.atomic.AtomicInteger progressCalls = new java.util.concurrent.atomic.AtomicInteger();
            ModelBatchMapRendererFactory factory = new ModelBatchMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "models/cube.g3dj",
                    p -> progressCalls.incrementAndGet()
            );
            MapRenderer renderer = factory.create(world);

            assertNotNull(renderer);
            assertTrue(loader.loaded);
            assertTrue(loader.updated);
            assertTrue(progressCalls.get() > 0);
        }
        world.dispose();
    }
}
