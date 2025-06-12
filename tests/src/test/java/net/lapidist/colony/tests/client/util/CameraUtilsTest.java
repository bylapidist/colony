package net.lapidist.colony.tests.client.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class CameraUtilsTest {
    private static final int TILE_X = 2;
    private static final int TILE_Y = 3;
    private static final int VIEW_WIDTH = 800;
    private static final int VIEW_HEIGHT = 600;
    private static final float POS = 5f;
    private static final float TOL = 0.0001f;

    @Test
    public void convertsBetweenWorldAndTileCoords() {
        Vector2 world = CameraUtils.tileCoordsToWorldCoords(TILE_X, TILE_Y);
        assertEquals(new Vector2(TILE_X * GameConstants.TILE_SIZE, TILE_Y * GameConstants.TILE_SIZE), world);
        Vector2 tile = CameraUtils.worldCoordsToTileCoords(world);
        assertEquals(new Vector2(TILE_X, TILE_Y), tile);

        Vector2 out = new Vector2();
        CameraUtils.tileCoordsToWorldCoords(TILE_X, TILE_Y, out);
        assertEquals(world, out);
        Vector2 back = new Vector2();
        CameraUtils.worldCoordsToTileCoords((int) out.x, (int) out.y, back);
        assertEquals(tile, back);
    }

    @Test
    public void calculatesViewBounds() {
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport vp = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, cam);
        cam.position.set(POS, POS, 0f);
        cam.update();
        Rectangle out = new Rectangle();
        CameraUtils.getViewBounds(cam, vp, out);
        assertEquals(POS - vp.getWorldWidth() / 2f, out.x, TOL);
        assertEquals(POS - vp.getWorldHeight() / 2f, out.y, TOL);
    }

    @Test
    public void screenWorldConversionsReuseVectors() {
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport vp = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, cam);
        Vector2 out = new Vector2();
        Vector2 result = CameraUtils.screenToWorldCoords(vp, 0f, 0f, out);
        assertSame(out, result);

        result = CameraUtils.worldToScreenCoords(
                vp,
                0f,
                0f,
                out,
                new com.badlogic.gdx.math.Vector3()
        );
        assertSame(out, result);

        Rectangle view = CameraUtils.getViewBounds(cam, vp, new Rectangle());
        assertTrue(CameraUtils.isVisible(view, out.x, out.y));
    }
}
