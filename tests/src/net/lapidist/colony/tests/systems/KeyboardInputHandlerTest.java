package net.lapidist.colony.tests.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class KeyboardInputHandlerTest {

    @Test
    public void usesConfiguredKeybinds() {
        Input input = mock(Input.class);
        Gdx.input = input;

        Settings settings = new Settings();
        settings.setKey(KeyAction.MOVE_UP, Input.Keys.Z);

        PlayerCameraSystem cam = new PlayerCameraSystem();
        KeyboardInputHandler handler = new KeyboardInputHandler(cam, settings);

        when(input.isKeyPressed(Input.Keys.Z)).thenReturn(true);

        float startY = cam.getCamera().position.y;
        handler.handleKeyboardInput(1f);
        assertTrue(cam.getCamera().position.y > startY);
    }
}
