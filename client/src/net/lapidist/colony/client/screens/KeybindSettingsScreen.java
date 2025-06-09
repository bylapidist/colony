package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.Settings;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Screen allowing users to remap keybinds.
 */
public final class KeybindSettingsScreen extends BaseScreen {
    private final Colony colony;
    private final Map<Settings.Action, TextButton> buttons = new EnumMap<>(Settings.Action.class);
    private Settings.Action awaiting;

    public KeybindSettingsScreen(final Colony game) {
        this.colony = game;

        for (Settings.Action action : Settings.Action.values()) {
            TextButton button = new TextButton(buttonLabel(action), getSkin());
            buttons.put(action, button);
            getRoot().add(button).row();
            button.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    awaiting = action;
                    button.setText(I18n.get("keybind.press"));
                }
            });
        }

        TextButton reset = new TextButton(I18n.get("keybind.reset"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());
        getRoot().add(reset).row();
        getRoot().add(back).row();

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.getSettings().resetKeybinds();
                updateLabels();
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

        getStage().addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyDown(final com.badlogic.gdx.scenes.scene2d.InputEvent event, final int keycode) {
                if (awaiting != null) {
                    colony.getSettings().setKey(awaiting, keycode);
                    updateLabels();
                    try {
                        colony.getSettings().save();
                    } catch (IOException e) {
                        // ignore
                    }
                    awaiting = null;
                    return true;
                }
                return false;
            }
        });
    }

    private void updateLabels() {
        for (Settings.Action action : Settings.Action.values()) {
            buttons.get(action).setText(buttonLabel(action));
        }
    }

    private String buttonLabel(final Settings.Action action) {
        return I18n.get("keybind." + action.name().toLowerCase()) + ": "
                + com.badlogic.gdx.Input.Keys.toString(colony.getSettings().getKey(action));
    }
}
