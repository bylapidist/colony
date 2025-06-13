package net.lapidist.colony.client.ui;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

/**
 * Basic tests for {@link MinimapActor}.
 */
@RunWith(GdxTestRunner.class)
public class MinimapActorTest {

    @Test
    public void drawsWithoutErrors() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem())
                .build());
        world.process();

        MinimapActor minimap = new MinimapActor(world);
        minimap.dispose();
    }

    @Test
    public void overlayRenderedAndCacheInvalidated() throws Exception {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem())
                .build());
        world.process();

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            MinimapActor actor = new MinimapActor(world);

            Stage stage = new Stage(new ScreenViewport(), new SpriteBatch());
            actor.setStage(stage);

            MinimapCache cache = mock(MinimapCache.class);
            java.lang.reflect.Field cf = MinimapActor.class.getDeclaredField("cache");
            cf.setAccessible(true);
            cf.set(actor, cache);

            ViewportOverlayRenderer renderer = mock(ViewportOverlayRenderer.class);
            java.lang.reflect.Field rf = MinimapActor.class.getDeclaredField("overlayRenderer");
            rf.setAccessible(true);
            rf.set(actor, renderer);

            MinimapOutlineRenderer outline = mock(MinimapOutlineRenderer.class);
            java.lang.reflect.Field of = MinimapActor.class.getDeclaredField("outlineRenderer");
            of.setAccessible(true);
            of.set(actor, outline);

            SpriteBatch batch = new SpriteBatch();

            actor.invalidateCache();
            verify(cache).invalidate();

            actor.draw(batch, 1f);

            verify(cache).setViewport(actor.getWidth(), actor.getHeight());
            verify(cache).ensureCache(any(), any(), any(), any(), anyFloat(), anyFloat());
            verify(cache).draw(any(SpriteBatch.class), eq(actor.getX()), eq(actor.getY()));
            verify(renderer).render(
                    same(batch), anyFloat(), anyFloat(), anyFloat(), anyFloat(),
                    eq(actor.getX()), eq(actor.getY())
            );
            verify(outline).render(batch, actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());

            actor.dispose();
            batch.dispose();
            stage.dispose();

            verify(outline).dispose();
        }
    }

    @Test
    public void logsWarningWhenTextureLoadFails() throws Exception {
        try (MockedConstruction<TextureAtlasResourceLoader> mocked =
                mockConstruction(TextureAtlasResourceLoader.class, (mock, ctx) ->
                        doThrow(new java.io.IOException("fail")).when(mock)
                                .loadTextures(any(), any(), any()))) {
            Logger logger = (Logger) LoggerFactory.getLogger(MinimapActor.class);
            ListAppender<ILoggingEvent> appender = new ListAppender<>();
            appender.start();
            logger.addAppender(appender);

            World world = new World(new WorldConfigurationBuilder()
                    .with(new MapLoadSystem(new MapState()), new PlayerCameraSystem())
                    .build());
            world.process();

            MinimapActor actor = new MinimapActor(world);
            actor.dispose();

            logger.detachAppender(appender);
            assertEquals(1, appender.list.size());
            assertEquals(Level.WARN, appender.list.get(0).getLevel());
        }
    }
}
