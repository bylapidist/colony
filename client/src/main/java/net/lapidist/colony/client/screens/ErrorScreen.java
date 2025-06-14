package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;

/**
 * Screen displayed when a critical error occurs.
 */
public final class ErrorScreen extends BaseScreen {
    public ErrorScreen(final Colony colony, final String message) {
        Label label = new Label(message, getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        getRoot().add(label).row();
        getRoot().add(back).row();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    colony.setScreen(new MainMenuScreen(colony));
                    return true;
                }
                return false;
            }
        });
    }
}
