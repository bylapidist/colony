package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MainMenuScreen extends BaseScreen {
    private final Colony colony;
    private static final float LOGO_PADDING = 10f;

    public MainMenuScreen(final Colony game) {
        this.colony = game;
        float scale = game.getSettings() == null ? 1f : game.getSettings().getUiScale();
        getStage().getRoot().setScale(scale);

        Image logo = new Image(new TextureRegionDrawable(new Texture(Gdx.files.internal("textures/logo.png"))));
        TextButton continueButton = createButton(I18n.get("main.continue"));
        TextButton newGameButton = createButton(I18n.get("main.newGame"));
        TextButton loadGameButton = createButton(I18n.get("main.loadGame"));
        TextButton settingsButton = createButton(I18n.get("main.settings"));
        TextButton exitButton = createButton(I18n.get("main.exit"));

        String lastSave = null;
        boolean canContinue = false;
        try {
            Path marker = Paths.get().getLastAutosaveMarker();
            if (Files.exists(marker)) {
                lastSave = Files.readString(marker).trim();
                canContinue = Files.exists(Paths.get().getAutosave(lastSave));
            }
        } catch (IOException e) {
            // ignore missing last autosave marker
        }

        continueButton.setDisabled(!canContinue);

        getRoot().add(logo).padBottom(LOGO_PADDING).row();
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
                colony.setScreen(new ModSelectionScreen(colony));
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
