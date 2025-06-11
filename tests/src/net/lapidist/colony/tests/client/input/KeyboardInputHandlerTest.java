package net.lapidist.colony.tests.client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.components.GameConstants;
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

    @Test
    public void movesAndClampsCamera() {
        PlayerCameraSystem cameraSystem = new PlayerCameraSystem();
        ((OrthographicCamera) cameraSystem.getCamera()).position.set(0, 0, 0);

        KeyBindings bindings = new KeyBindings();
        KeyboardInputHandler handler = new KeyboardInputHandler(cameraSystem, bindings);

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyPressed(bindings.getKey(KeyAction.MOVE_RIGHT))).thenReturn(true);
        handler.handleKeyboardInput(DT);

        assertEquals(CAMERA_SPEED * DT, cameraSystem.getCamera().position.x, TOL);
        assertEquals(0f, cameraSystem.getCamera().position.y, TOL);

        cameraSystem.getCamera().position.set(
                GameConstants.MAP_WIDTH * GameConstants.TILE_SIZE + OFFSET_X,
                GameConstants.MAP_HEIGHT * GameConstants.TILE_SIZE + OFFSET_Y,
                0f
        );
        handler.clampCameraPosition();
        assertEquals(GameConstants.MAP_WIDTH * GameConstants.TILE_SIZE,
                cameraSystem.getCamera().position.x, TOL);
        assertEquals(GameConstants.MAP_HEIGHT * GameConstants.TILE_SIZE,
                cameraSystem.getCamera().position.y, TOL);
    }
}
