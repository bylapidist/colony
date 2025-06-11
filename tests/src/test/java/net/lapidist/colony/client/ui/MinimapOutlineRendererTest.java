package net.lapidist.colony.client.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link MinimapOutlineRenderer}.
 */
@RunWith(GdxTestRunner.class)
public class MinimapOutlineRendererTest {

    private static final float X = 1f;
    private static final float Y = 2f;
    private static final float W = 3f;
    private static final float H = 4f;

    private static void injectRenderer(
            final MinimapOutlineRenderer renderer,
            final ShapeRenderer sr
    ) throws Exception {
        Field f = MinimapOutlineRenderer.class.getDeclaredField("shapeRenderer");
        f.setAccessible(true);
        f.set(renderer, sr);
    }

    @Test
    public void drawsOutlineWhenRendererPresent() throws Exception {
        MinimapOutlineRenderer renderer = new MinimapOutlineRenderer();
        ShapeRenderer sr = mock(ShapeRenderer.class);
        injectRenderer(renderer, sr);
        SpriteBatch batch = mock(SpriteBatch.class);
        Matrix4 proj = new Matrix4();
        when(batch.getProjectionMatrix()).thenReturn(proj);

        renderer.render(batch, X, Y, W, H);

        verify(batch).end();
        verify(sr).setProjectionMatrix(proj);
        verify(sr).begin(ShapeRenderer.ShapeType.Line);
        verify(sr, times(2)).rect(anyFloat(), anyFloat(), anyFloat(), anyFloat());
        verify(sr).end();
        verify(batch).begin();
    }

    @Test
    public void skipsRenderingWhenNull() {
        MinimapOutlineRenderer renderer = new MinimapOutlineRenderer();
        SpriteBatch batch = mock(SpriteBatch.class);
        renderer.render(batch, X, X, Y, Y);
        verifyNoInteractions(batch);
    }

    @Test
    public void disposesRenderer() throws Exception {
        MinimapOutlineRenderer renderer = new MinimapOutlineRenderer();
        ShapeRenderer sr = mock(ShapeRenderer.class);
        injectRenderer(renderer, sr);
        renderer.dispose();
        verify(sr).dispose();
    }
}
