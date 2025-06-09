package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.KeybindsScreen;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class KeybindsScreenTest {

    private static void setSettings(final Colony colony, final Settings settings) throws Exception {
        Field f = Colony.class.getDeclaredField("settings");
        f.setAccessible(true);
        f.set(colony, settings);
    }

    private static <T> T getField(final Object obj, final String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        T value = (T) f.get(obj);
        return value;
    }

    @Test
    public void remapsKeyOnInput() throws Exception {
        Settings settings = new Settings();
        Colony colony = new Colony();
        setSettings(colony, settings);

        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        KeybindsScreen screen = new KeybindsScreen(colony, stage);
        Map<KeyAction, TextButton> buttons = getField(screen, "buttons");
        TextButton gatherButton = buttons.get(KeyAction.GATHER);
        gatherButton.fire(new ChangeListener.ChangeEvent());

        stage.keyDown(Input.Keys.G);

        assertEquals(Input.Keys.G, settings.getKeyBindings().getKey(KeyAction.GATHER));
    }
}
