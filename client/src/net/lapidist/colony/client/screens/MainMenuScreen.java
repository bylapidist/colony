package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MainMenuScreen extends BaseScreen {
    private final Colony colony;

    public MainMenuScreen(final Colony game) {
        this.colony = game;

        TextButton continueButton = new TextButton(I18n.get("main.continue"), getSkin());
        TextButton newGameButton = new TextButton(I18n.get("main.newGame"), getSkin());
        TextButton loadGameButton = new TextButton(I18n.get("main.loadGame"), getSkin());
        TextButton settingsButton = new TextButton(I18n.get("main.settings"), getSkin());
        TextButton exitButton = new TextButton(I18n.get("main.exit"), getSkin());

        String lastSave = null;
        boolean canContinue = false;
        try {
            Path marker = Paths.getLastAutosaveMarker();
            if (Files.exists(marker)) {
                lastSave = Files.readString(marker).trim();
                canContinue = Files.exists(Paths.getAutosave(lastSave));
            }
        } catch (IOException e) {
            // ignore missing last autosave marker
        }

        continueButton.setDisabled(!canContinue);

        getRoot().add(continueButton).row();
        getRoot().add(newGameButton).row();
        getRoot().add(loadGameButton).row();
        getRoot().add(settingsButton).row();
        getRoot().add(exitButton).row();

        final String last = lastSave;
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                if (!continueButton.isDisabled()) {
                    colony.startGame(last);
                }
            }
        });

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new NewGameScreen(colony));
            }
        });

        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new LoadGameScreen(colony));
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new SettingsScreen(colony));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                Gdx.app.exit();
            }
        });
    }

}
