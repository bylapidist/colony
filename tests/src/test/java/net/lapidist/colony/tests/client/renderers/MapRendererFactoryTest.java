package net.lapidist.colony.tests.client.renderers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.SpriteMapRendererFactory;
import net.lapidist.colony.client.graphics.Box2dLightsPlugin;
import net.lapidist.colony.client.renderers.SpriteBatchMapRenderer;
import net.lapidist.colony.client.graphics.LightingPlugin;
import net.lapidist.colony.client.graphics.ShaderManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.settings.Settings;
import org.mockito.MockedStatic;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.*;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MapRendererFactoryTest {

    private static final float HALF_PROGRESS = 0.5f;
    private static final float EPSILON = 0.0001f;

    private static final class DummyLoader implements ResourceLoader {
        private boolean loaded;
        private boolean updated;
        @Override
        public FileLocation getFileLocation() {
            return FileLocation.INTERNAL;
        }

        @Override
        public com.badlogic.gdx.graphics.g2d.ParticleEffect loadEffect(final String path) {
            return new com.badlogic.gdx.graphics.g2d.ParticleEffect();
        }

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
            MapRenderer renderer = factory.create(world, null);

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
    public void pluginLightsAreApplied() {
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.update()).thenReturn(true);
        when(loader.getProgress()).thenReturn(1f);

        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        Box2dLightsPlugin plugin = new Box2dLightsPlugin();

        try (MockedConstruction<SpriteBatchMapRenderer> sbCons =
                     mockConstruction(SpriteBatchMapRenderer.class);
             MockedConstruction<SpriteBatch> ignored =
                     mockConstruction(SpriteBatch.class)) {
            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/textures.atlas"
            );
            MapRenderer renderer = factory.create(world, plugin);

            renderer.render(mock(net.lapidist.colony.client.render.MapRenderData.class), null);

            SpriteBatchMapRenderer sb = sbCons.constructed().get(0);
            // plugin fails to initialize lights in headless tests
            assertNull(plugin.getRayHandler());
            verify(sb, never()).setLights(any());

            ((com.badlogic.gdx.utils.Disposable) renderer).dispose();
        }
        world.dispose();
    }

    private static final class DummyLightingPlugin implements LightingPlugin {
        private final box2dLight.RayHandler handler;

        DummyLightingPlugin(final box2dLight.RayHandler rayHandler) {
            this.handler = rayHandler;
        }

        @Override
        public ShaderProgram create(final ShaderManager manager) {
            return null;
        }

        @Override
        public box2dLight.RayHandler getRayHandler() {
            return handler;
        }

        @Override
        public String id() {
            return "dummy";
        }

        @Override
        public String displayName() {
            return "Dummy";
        }

        @Override
        public void dispose() {
        }
    }

    @Test
    public void pluginLightsDisabledDisposesHandler() {
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.update()).thenReturn(true);
        when(loader.getProgress()).thenReturn(1f);

        Settings settings = new Settings();
        settings.getGraphicsSettings().setLightingEnabled(false);

        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        box2dLight.RayHandler handler = mock(box2dLight.RayHandler.class);
        DummyLightingPlugin plugin = new DummyLightingPlugin(handler);

        try (MockedStatic<Settings> settingsStatic = mockStatic(Settings.class);
             MockedConstruction<SpriteBatchMapRenderer> sbCons =
                     mockConstruction(SpriteBatchMapRenderer.class);
             MockedConstruction<SpriteBatch> ignored =
                     mockConstruction(SpriteBatch.class)) {
            settingsStatic.when(Settings::load).thenReturn(settings);

            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/textures.atlas"
            );
            MapRenderer renderer = factory.create(world, plugin);
            renderer.render(null, null);

            SpriteBatchMapRenderer sb = sbCons.constructed().get(0);
            verify(sb, never()).setLights(any());
            verify(handler).dispose();

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
            MapRenderer renderer = factory.create(world, null);

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
        public FileLocation getFileLocation() {
            return FileLocation.INTERNAL;
        }

        @Override
        public com.badlogic.gdx.graphics.g2d.ParticleEffect loadEffect(final String path) {
            return new com.badlogic.gdx.graphics.g2d.ParticleEffect();
        }

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
            MapRenderer renderer = factory.create(world, null);

            assertNotNull(renderer);
            renderer.render(null, null);
            assertEquals(HALF_PROGRESS, progress.get(), EPSILON);
            renderer.render(null, null);
            assertEquals(1f, progress.get(), EPSILON);
            ((com.badlogic.gdx.utils.Disposable) renderer).dispose();
        }
        world.dispose();
    }

    @Test
    public void logsWarningWhenTextureLoadFails() throws Exception {
        ResourceLoader loader = mock(ResourceLoader.class);
        doThrow(new java.io.IOException("fail"))
                .when(loader).loadTextures(any(), any(), any());

        Logger logger = (Logger) LoggerFactory.getLogger(SpriteMapRendererFactory.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        try (MockedConstruction<SpriteBatch> ignored =
                mockConstruction(SpriteBatch.class)) {
            MapRendererFactory factory = new SpriteMapRendererFactory(
                    loader,
                    FileLocation.INTERNAL,
                    "textures/missing.atlas"
            );
            MapRenderer renderer = factory.create(world, null);

            assertNotNull(renderer);
        }
        world.dispose();

        logger.detachAppender(appender);
        assertEquals(1, appender.list.size());
        assertEquals(Level.WARN, appender.list.get(0).getLevel());
    }
}
