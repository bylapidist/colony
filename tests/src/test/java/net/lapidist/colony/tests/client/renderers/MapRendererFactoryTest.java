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

    private static final float HALF_PROGRESS = 0.5f;
    private static final float EPSILON = 0.0001f;

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
    public void createLoadsResources() {
        DummyLoader loader = new DummyLoader();
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        try (MockedConstruction<SpriteBatch> ignored =
                mockConstruction(SpriteBatch.class)) {
            java.util.concurrent.atomic.AtomicInteger progressCalls = new java.util.concurrent.atomic.AtomicInteger();
            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/textures.atlas",
                    p -> progressCalls.incrementAndGet()
            );
            MapRenderer renderer = factory.create(world);

            assertNotNull(renderer);
            assertFalse(loader.updated);
            renderer.render(null, null);
            assertTrue(loader.updated);
            assertTrue(progressCalls.get() > 0);
            ((com.badlogic.gdx.utils.Disposable) renderer).dispose();
        }
        world.dispose();
    }

    @Test
    public void createWithoutCallback() {
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
            assertFalse(loader.updated);
            renderer.render(null, null);
            assertTrue(loader.updated);
            ((com.badlogic.gdx.utils.Disposable) renderer).dispose();
        }
        world.dispose();
    }

    private static final class SlowLoader implements ResourceLoader {
        private int calls;
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
            calls++;
            updated = true;
            if (calls == 1) {
                loaded = false;
                return false;
            }
            loaded = true;
            return true;
        }

        @Override
        public float getProgress() {
            return calls == 1 ? HALF_PROGRESS : 1f;
        }

        @Override
        public void dispose() {
        }
    }

    @Test
    public void delayedLoadReportsProgress() {
        SlowLoader loader = new SlowLoader();
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        try (MockedConstruction<SpriteBatch> ignored =
                mockConstruction(SpriteBatch.class)) {
            java.util.concurrent.atomic.AtomicReference<Float> progress =
                    new java.util.concurrent.atomic.AtomicReference<>(0f);
            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/textures.atlas",
                    progress::set
            );
            MapRenderer renderer = factory.create(world);

            assertNotNull(renderer);
            renderer.render(null, null);
            assertEquals(HALF_PROGRESS, progress.get(), EPSILON);
            renderer.render(null, null);
            assertEquals(1f, progress.get(), EPSILON);
            ((com.badlogic.gdx.utils.Disposable) renderer).dispose();
        }
        world.dispose();
    }
}
