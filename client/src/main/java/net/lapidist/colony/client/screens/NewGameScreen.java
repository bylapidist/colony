package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.map.MapSize;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;

import java.util.UUID;

public final class NewGameScreen extends BaseScreen {
    private static final float FIELD_WIDTH = 200f;
    private final Colony colony;

    public NewGameScreen(final Colony game) {
        this.colony = game;

        Label label = new Label(I18n.get("newGame.saveName"), getSkin());
        TextField nameField = new TextField("", getSkin());
        TextButton startButton = new TextButton(I18n.get("newGame.start"), getSkin());
        TextButton backButton = new TextButton(I18n.get("common.back"), getSkin());

        Label sizeLabel = new Label(I18n.get("newGame.mapSize"), getSkin());
        TextButton small = new TextButton(I18n.get("mapSize.small"), getSkin(), "toggle");
        TextButton medium = new TextButton(I18n.get("mapSize.medium"), getSkin(), "toggle");
        TextButton large = new TextButton(I18n.get("mapSize.large"), getSkin(), "toggle");
        TextButton veryLarge = new TextButton(I18n.get("mapSize.veryLarge"), getSkin(), "toggle");
        ButtonGroup<TextButton> group = new ButtonGroup<>(small, medium, large, veryLarge);
        group.setMaxCheckCount(1);
        group.setMinCheckCount(1);
        group.setUncheckLast(true);
        small.setChecked(true);

        getRoot().add(label).row();
        getRoot().add(nameField).width(FIELD_WIDTH).row();
        getRoot().add(sizeLabel).row();
        getRoot().add(small).row();
        getRoot().add(medium).row();
        getRoot().add(large).row();
        getRoot().add(veryLarge).row();
        getRoot().add(startButton).row();
        getRoot().add(backButton).row();

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                String name = nameField.getText();
                if (name == null || name.isEmpty()) {
                    name = "save-" + UUID.randomUUID();
                }
                MapSize size = MapSize.SMALL;
                if (medium.isChecked()) {
                    size = MapSize.MEDIUM;
                } else if (large.isChecked()) {
                    size = MapSize.LARGE;
                } else if (veryLarge.isChecked()) {
                    size = MapSize.VERY_LARGE;
                }
                colony.startGame(name, size.width(), size.height());
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
