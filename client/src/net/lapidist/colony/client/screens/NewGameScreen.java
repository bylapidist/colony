package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;

import java.util.UUID;

public final class NewGameScreen extends BaseScreen {
    private static final float FIELD_WIDTH = 200f;
    private final Colony colony;

    public NewGameScreen(final Colony game) {
        this.colony = game;

        Label label = new Label("Save name", getSkin());
        TextField nameField = new TextField("", getSkin());
        TextButton startButton = new TextButton("Start", getSkin());
        TextButton backButton = new TextButton("Back", getSkin());

        getRoot().add(label).row();
        getRoot().add(nameField).width(FIELD_WIDTH).row();
        getRoot().add(startButton).row();
        getRoot().add(backButton).row();

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                String name = nameField.getText();
                if (name == null || name.isEmpty()) {
                    name = "save-" + UUID.randomUUID();
                }
                colony.startGame(name);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });
    }

}
