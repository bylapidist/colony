package net.lapidist.colony.client.renderers;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests for {@link LoadingSpriteMapRenderer}. */
@RunWith(GdxTestRunner.class)
public class LoadingSpriteMapRendererTest {

    @Test
    public void passesLightsToDelegate() {
        ResourceLoader loader = mock(ResourceLoader.class);
        when(loader.update()).thenReturn(true);
        when(loader.getProgress()).thenReturn(1f);

        World world = new World(new WorldConfigurationBuilder().build());
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        RayHandler lights = mock(RayHandler.class);

        try (MockedConstruction<SpriteBatchMapRenderer> cons =
                mockConstruction(SpriteBatchMapRenderer.class)) {
            LoadingSpriteMapRenderer renderer = new LoadingSpriteMapRenderer(
                    world, batch, loader, camera, false, null, null);
            renderer.setLights(lights);

            renderer.render(mock(net.lapidist.colony.client.render.MapRenderData.class), null);

            SpriteBatchMapRenderer sb = cons.constructed().get(0);
            verify(sb).setLights(lights);

            renderer.dispose();
            verify(lights).dispose();
            verify(sb).dispose();
            verify(loader, never()).dispose();
        }
        world.dispose();
    }
}
