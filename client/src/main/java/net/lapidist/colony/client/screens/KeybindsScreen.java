package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Screen allowing key remapping.
 */
public final class KeybindsScreen extends BaseScreen {
    private final Colony colony;
    private final Map<KeyAction, TextButton> buttons = new EnumMap<>(KeyAction.class);
    private KeyAction awaiting;

    public KeybindsScreen(final Colony game) {
        this(game, new Stage(new ScreenViewport()));
    }

    public KeybindsScreen(final Colony game, final Stage stage) {
        super(stage);
        this.colony = game;
        float scale = game.getSettings() == null ? 1f : game.getSettings().getUiScale();
        stage.getRoot().setScale(scale);
        KeyBindings bindings = game.getSettings().getKeyBindings();
        Table root = getRoot();
        Table list = new Table();

        for (KeyAction action : KeyAction.values()) {
            String label = I18n.get("keybind." + action.getI18nKey());
            TextButton btn = new TextButton(label + ": " + Input.Keys.toString(bindings.getKey(action)), getSkin());
            buttons.put(action, btn);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    awaiting = action;
                    btn.setText(label + ": ?");
                }
            });
            list.add(btn).row();
        }

        ScrollPane scroll = new ScrollPane(list, getSkin());
        scroll.setScrollingDisabled(true, false);
        root.add(scroll).expand().fill().row();

        TextButton reset = new TextButton(I18n.get("common.reset"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                bindings.reset();
                updateButtons();
                save();
            }
        });
        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new SettingsScreen(colony));
            }
        });

        root.add(reset).row();
        root.add(back).row();

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (awaiting != null) {
                    bindings.setKey(awaiting, keycode);
                    String label = I18n.get("keybind." + awaiting.getI18nKey());
                    buttons.get(awaiting).setText(label + ": " + Input.Keys.toString(keycode));
                    awaiting = null;
                    save();
                    return true;
                }
                return false;
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (awaiting == null && keycode == Input.Keys.ESCAPE) {
                    colony.setScreen(new SettingsScreen(colony));
                    return true;
                }
                return false;
            }
        });
    }

    private void updateButtons() {
        KeyBindings bindings = colony.getSettings().getKeyBindings();
        for (KeyAction action : KeyAction.values()) {
            String label = I18n.get("keybind." + action.getI18nKey());
            buttons.get(action).setText(label + ": " + Input.Keys.toString(bindings.getKey(action)));
        }
    }

    private void save() {
        try {
            colony.getSettings().save();
        } catch (IOException e) {
            // ignore
        }
    }
}
