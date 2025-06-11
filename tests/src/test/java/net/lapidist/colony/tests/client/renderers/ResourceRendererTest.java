package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.mockito.ArgumentCaptor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.renderers.ResourceRenderer;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ResourceRendererTest {

    private static final int WOOD = 1;
    private static final int STONE = 2;
    private static final int FOOD = 3;

    @Test
    public void rendersResourceTextWithoutErrors() {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build());

        renderer.render(tiles);
        renderer.dispose();
    }

    @Test
    public void drawsResourceTextForSelectedTile() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        BitmapFont font = spy(new BitmapFont());
        GlyphLayout layout = spy(new GlyphLayout());
        java.lang.reflect.Field fontField = ResourceRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        fontField.set(renderer, font);
        java.lang.reflect.Field layoutField = ResourceRenderer.class.getDeclaredField("layout");
        layoutField.setAccessible(true);
        layoutField.set(renderer, layout);

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(true)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build());

        renderer.render(tiles);

        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(layout).setText(eq(font), captor.capture());
        assertEquals(WOOD + "/" + STONE + "/" + FOOD, captor.getValue().toString());
        verify(font).draw(eq(batch), eq(layout), anyFloat(), anyFloat());
        renderer.dispose();
    }

    @Test
    public void skipsUnselectedTiles() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        BitmapFont font = spy(new BitmapFont());
        java.lang.reflect.Field fontField = ResourceRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        fontField.set(renderer, font);

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build());

        renderer.render(tiles);

        verify(font, never()).draw(any(SpriteBatch.class), any(GlyphLayout.class), anyFloat(), anyFloat());
        renderer.dispose();
    }
}
