package net.lapidist.colony.client.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class ViewportOverlayRendererTest {

    private static final float CAM_POS = 50f;
    private static final float VIEW_SIZE = 100f;
    private static final float MAP_WIDTH = 200f;
    private static final float MAP_HEIGHT = 100f;
    private static final float SCALE = 0.5f;
    private static final float OFFSET_X = 10f;
    private static final float OFFSET_Y = 20f;
    private static final float CLAMP_WIDTH = 40f;
    private static final float CLAMP_HEIGHT = 30f;
    private static final float DIV = 10f;

    private static void injectRenderer(
            final ViewportOverlayRenderer renderer,
            final ShapeRenderer sr
    ) throws Exception {
        Field f = ViewportOverlayRenderer.class.getDeclaredField("shapeRenderer");
        f.setAccessible(true);
        f.set(renderer, sr);
    }

    @Test
    public void drawsRectangleWhenCameraActive() throws Exception {
        OrthographicCamera cam = new OrthographicCamera();
        cam.position.set(CAM_POS, CAM_POS, 0f);
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        PlayerCameraSystem system = mock(PlayerCameraSystem.class);
        when(system.getCamera()).thenReturn(cam);
        when(system.getViewport()).thenReturn(viewport);

        ViewportOverlayRenderer renderer = new ViewportOverlayRenderer(system);
        ShapeRenderer sr = mock(ShapeRenderer.class);
        injectRenderer(renderer, sr);

        SpriteBatch batch = mock(SpriteBatch.class);
        Matrix4 proj = new Matrix4();
        when(batch.getProjectionMatrix()).thenReturn(proj);

        renderer.render(batch, MAP_WIDTH, MAP_HEIGHT, SCALE, SCALE, OFFSET_X, OFFSET_Y);

        verify(batch).end();
        verify(sr).setProjectionMatrix(proj);
        verify(sr).begin(ShapeRenderer.ShapeType.Line);
        verify(sr).rect(anyFloat(), anyFloat(), anyFloat(), anyFloat());
        verify(sr).end();
        verify(batch).begin();
    }

    @Test
    public void skipsRenderingWhenNull() throws Exception {
        ViewportOverlayRenderer renderer = new ViewportOverlayRenderer(null);
        ShapeRenderer sr = mock(ShapeRenderer.class);
        injectRenderer(renderer, sr);
        SpriteBatch batch = mock(SpriteBatch.class);
        renderer.render(batch, VIEW_SIZE, VIEW_SIZE, SCALE, SCALE, 0f, 0f);
        verifyNoInteractions(sr);
        verifyNoInteractions(batch);

        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE / DIV, VIEW_SIZE / DIV, cam);
        PlayerCameraSystem system = mock(PlayerCameraSystem.class);
        when(system.getCamera()).thenReturn(cam);
        when(system.getViewport()).thenReturn(viewport);
        ViewportOverlayRenderer renderer2 = new ViewportOverlayRenderer(system);
        SpriteBatch batch2 = mock(SpriteBatch.class);
        renderer2.render(batch2, VIEW_SIZE / DIV, VIEW_SIZE / DIV, SCALE, SCALE, 0f, 0f);
        verifyNoInteractions(batch2);
    }

    @Test
    public void clampsRectangleToMapBounds() throws Exception {
        OrthographicCamera cam = new OrthographicCamera();
        cam.position.set(CAM_POS, CAM_POS, 0f);
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        PlayerCameraSystem system = mock(PlayerCameraSystem.class);
        when(system.getCamera()).thenReturn(cam);
        when(system.getViewport()).thenReturn(viewport);

        ViewportOverlayRenderer renderer = new ViewportOverlayRenderer(system);
        ShapeRenderer sr = mock(ShapeRenderer.class);
        injectRenderer(renderer, sr);

        SpriteBatch batch = mock(SpriteBatch.class);
        when(batch.getProjectionMatrix()).thenReturn(new Matrix4());

        renderer.render(batch, CLAMP_WIDTH, CLAMP_HEIGHT, 1f, 1f, 0f, 0f);

        ArgumentCaptor<Float> xCap = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> yCap = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> wCap = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> hCap = ArgumentCaptor.forClass(Float.class);
        verify(sr).rect(xCap.capture(), yCap.capture(), wCap.capture(), hCap.capture());
        assertTrue(xCap.getValue() >= 0f);
        assertTrue(yCap.getValue() >= 0f);
        assertTrue(wCap.getValue() <= CLAMP_WIDTH);
        assertTrue(hCap.getValue() <= CLAMP_HEIGHT);
    }
}
