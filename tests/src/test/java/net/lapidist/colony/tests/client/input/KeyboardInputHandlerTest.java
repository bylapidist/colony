package net.lapidist.colony.tests.client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class KeyboardInputHandlerTest {
    private static final float DT = 1f;
    private static final float TOL = 0.001f;
    private static final float CAMERA_SPEED = 400f;
    private static final float OFFSET_X = 10f;
    private static final float OFFSET_Y = 5f;
    private static final int VIEW_SIZE = 160;

    @Test
    public void movesAndClampsCamera() {
        PlayerCameraSystem cameraSystem = new PlayerCameraSystem();
        cameraSystem.toggleMode();
        ((OrthographicCamera) cameraSystem.getCamera()).position.set(0, 0, 0);
        ((com.badlogic.gdx.utils.viewport.ExtendViewport) cameraSystem.getViewport())
                .update(VIEW_SIZE, VIEW_SIZE, false);

        KeyBindings bindings = new KeyBindings();
        KeyboardInputHandler handler = new KeyboardInputHandler(cameraSystem, bindings);

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyPressed(bindings.getKey(KeyAction.MOVE_RIGHT))).thenReturn(true);
        handler.handleKeyboardInput(DT);

        assertEquals(CAMERA_SPEED * DT, cameraSystem.getCamera().position.x, TOL);
        assertEquals(0f, cameraSystem.getCamera().position.y, TOL);

        cameraSystem.getCamera().position.set(
                MapState.DEFAULT_WIDTH * GameConstants.TILE_SIZE + OFFSET_X,
                MapState.DEFAULT_HEIGHT * GameConstants.TILE_SIZE + OFFSET_Y,
                0f
        );
        handler.clampCameraPosition();

        float mapWidth = MapState.DEFAULT_WIDTH * GameConstants.TILE_SIZE;
        float mapHeight = MapState.DEFAULT_HEIGHT * GameConstants.TILE_SIZE;

        assertTrue(cameraSystem.getCamera().position.x <= mapWidth);
        assertTrue(cameraSystem.getCamera().position.x >= 0f);
        assertTrue(cameraSystem.getCamera().position.y <= mapHeight);
        assertTrue(cameraSystem.getCamera().position.y >= 0f);
    }
}
