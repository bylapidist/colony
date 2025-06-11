package net.lapidist.colony.client.ui;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;

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

            actor.dispose();
            batch.dispose();
            stage.dispose();
        }
    }
}
