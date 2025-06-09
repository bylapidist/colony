package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.KeyAction;

import java.io.IOException;
import java.util.EnumMap;

/** Screen allowing keybind configuration. */
public final class KeybindsScreen extends BaseScreen {
    private final Colony colony;
    private final EnumMap<KeyAction, TextButton> buttons = new EnumMap<>(KeyAction.class);
    private KeyAction editing;

    public KeybindsScreen(final Colony game) {
        this.colony = game;

        Table root = getRoot();
        for (KeyAction action : KeyAction.values()) {
            Label label = new Label(I18n.get("keybind." + action.name().toLowerCase()), getSkin());
            TextButton btn = new TextButton(keyName(action), getSkin());
            buttons.put(action, btn);
            root.add(label).pad(2f);
            root.add(btn).row();
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    editing = action;
                }
            });
        }
        TextButton reset = new TextButton(I18n.get("keybind.reset"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());
        root.add(reset).colspan(2).row();
        root.add(back).colspan(2).row();

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.getSettings().resetKeybinds();
                updateButtons();
                try {
                    colony.getSettings().save();
                } catch (IOException e) {
                    // ignore
                }
            }
        });
        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new SettingsScreen(colony));
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (editing != null) {
                    colony.getSettings().setKey(editing, keycode);
                    buttons.get(editing).setText(keyName(editing));
                    try {
                        colony.getSettings().save();
                    } catch (IOException e) {
                        // ignore
                    }
                    editing = null;
                    return true;
                }
                return false;
            }
        });
    }

    private String keyName(final KeyAction action) {
        return com.badlogic.gdx.Input.Keys.toString(colony.getSettings().getKey(action));
    }

    private void updateButtons() {
        for (KeyAction a : KeyAction.values()) {
            buttons.get(a).setText(keyName(a));
        }
    }
}
