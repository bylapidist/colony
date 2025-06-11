package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.render.data.RenderTile;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class TileRendererTest {

    @Test
    public void drawsTileAndOverlay() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver());

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(true)
                .wood(0)
                .stone(0)
                .food(0)
                .build());

        renderer.render(tiles);

        verify(batch, times(2)).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }
}
