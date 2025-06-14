package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.components.GameConstants;

import java.util.UUID;

public final class NewGameScreen extends BaseScreen {
    private static final float FIELD_WIDTH = 200f;
    private static final int SMALL_SIZE = 30;
    private static final int MEDIUM_SIZE = 60;
    private static final int LARGE_SIZE = 90;
    private static final int HUGE_SIZE = 120;
    private final Colony colony;
    private int width = GameConstants.MAP_WIDTH;
    private int height = GameConstants.MAP_HEIGHT;

    public NewGameScreen(final Colony game) {
        this.colony = game;

        Label label = new Label(I18n.get("newGame.saveName"), getSkin());
        TextField nameField = new TextField("", getSkin());
        TextButton smallButton = new TextButton(I18n.get("newGame.sizeSmall"), getSkin());
        TextButton mediumButton = new TextButton(I18n.get("newGame.sizeMedium"), getSkin());
        TextButton largeButton = new TextButton(I18n.get("newGame.sizeLarge"), getSkin());
        TextButton hugeButton = new TextButton(I18n.get("newGame.sizeHuge"), getSkin());
        TextButton startButton = new TextButton(I18n.get("newGame.start"), getSkin());
        TextButton backButton = new TextButton(I18n.get("common.back"), getSkin());

        getRoot().add(label).row();
        getRoot().add(nameField).width(FIELD_WIDTH).row();
        getRoot().add(smallButton).row();
        getRoot().add(mediumButton).row();
        getRoot().add(largeButton).row();
        getRoot().add(hugeButton).row();
        getRoot().add(startButton).row();
        getRoot().add(backButton).row();

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                String name = nameField.getText();
                if (name == null || name.isEmpty()) {
                    name = "save-" + UUID.randomUUID();
                }
                colony.startGame(name, width, height);
            }
        });

        smallButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                width = SMALL_SIZE;
                height = SMALL_SIZE;
            }
        });

        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                width = MEDIUM_SIZE;
                height = MEDIUM_SIZE;
            }
        });

        largeButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                width = LARGE_SIZE;
                height = LARGE_SIZE;
            }
        });

        hugeButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                width = HUGE_SIZE;
                height = HUGE_SIZE;
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
