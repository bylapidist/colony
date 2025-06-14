package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.components.state.MapState;

import java.util.UUID;

public final class NewGameScreen extends BaseScreen {
    private static final float FIELD_WIDTH = 200f;
    private static final int SMALL_SIZE = 30;
    private static final int MEDIUM_SIZE = 60;
    private static final int LARGE_SIZE = 90;
    private static final float PADDING = 10f;
    private static final int[] SIZES = {SMALL_SIZE, MEDIUM_SIZE, LARGE_SIZE};
    private static final String[] SIZE_KEYS = {
            "newGame.sizeSmall",
            "newGame.sizeMedium",
            "newGame.sizeLarge"
    };
    private final Colony colony;
    private int width = MapState.DEFAULT_WIDTH;
    private int height = MapState.DEFAULT_HEIGHT;
    private int sizeIndex = 0;

    public NewGameScreen(final Colony game) {
        this.colony = game;

        Label nameLabel = new Label(I18n.get("newGame.saveName"), getSkin());
        TextField nameField = new TextField("", getSkin());
        Label sizeLabel = new Label(I18n.get("newGame.mapSize"), getSkin());
        TextButton sizeButton = new TextButton(I18n.get(SIZE_KEYS[sizeIndex]), getSkin());
        TextButton startButton = new TextButton(I18n.get("newGame.start"), getSkin());
        TextButton backButton = new TextButton(I18n.get("common.back"), getSkin());

        Table options = new Table();
        options.add(nameLabel);
        options.add(nameField).width(FIELD_WIDTH).row();
        options.add(sizeLabel);
        options.add(sizeButton).row();

        ScrollPane scroll = new ScrollPane(options, getSkin());
        scroll.setScrollingDisabled(true, false);
        getRoot().add(scroll).expand().fill().row();

        Table buttons = new Table();
        buttons.add(backButton).padRight(PADDING);
        buttons.add(startButton);

        getRoot().add(buttons).padBottom(PADDING).bottom();

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

        sizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                sizeIndex = (sizeIndex + 1) % SIZES.length;
                sizeButton.setText(I18n.get(SIZE_KEYS[sizeIndex]));
                width = SIZES[sizeIndex];
                height = SIZES[sizeIndex];
            }
        });


        backButton.addListener(new ChangeListener() {
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
