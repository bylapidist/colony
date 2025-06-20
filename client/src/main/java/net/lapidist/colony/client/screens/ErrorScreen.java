package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.client.Colony;

/** Simple screen that displays an error message with a button to return to the main menu. */
public final class ErrorScreen extends BaseScreen {
    public ErrorScreen(final Colony colony, final String message) {
        Label label = createLabel(message);
        TextButton back = createButton(I18n.get("common.back"));

        getRoot().add(label).row();
        getRoot().add(back).row();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });
    }
}
