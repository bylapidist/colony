package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.server.io.Paths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LoadGameScreen extends BaseScreen {
    private final Colony colony;

    public LoadGameScreen(final Colony game) {
        this.colony = game;

        List<String> saves = listSaves();
        for (String save : saves) {
            TextButton button = new TextButton(save, getSkin());
            getRoot().add(button).row();
            button.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    colony.startGame(save);
                }
            });
        }

        TextButton backButton = new TextButton("Back", getSkin());
        getRoot().add(backButton).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });
    }

    private List<String> listSaves() {
        try {
            return Paths.listAutosaves();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
