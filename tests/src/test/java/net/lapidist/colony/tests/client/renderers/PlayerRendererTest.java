package net.lapidist.colony.tests.client.renderers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.client.renderers.PlayerRenderer;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerInitSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class PlayerRendererTest {

    @Test
    public void drawsPlayerTextureWhenPresent() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem(), new PlayerInitSystem())
                .build());
        world.process();

        PlayerRenderer renderer = new PlayerRenderer(batch, loader, world);
        renderer.render(null);

        verify(batch).draw(eq(region), anyFloat(), anyFloat());
        world.dispose();
    }

    @Test
    public void skipsRenderingWithoutPlayer() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem())
                .build());
        world.process();

        PlayerRenderer renderer = new PlayerRenderer(batch, loader, world);
        renderer.render(null);

        verifyNoInteractions(batch);
        world.dispose();
    }
}
