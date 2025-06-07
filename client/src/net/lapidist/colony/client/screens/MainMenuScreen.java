package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.server.io.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MainMenuScreen extends ScreenAdapter {

    private final Stage stage;
    private final Colony colony;

    public MainMenuScreen(final Colony game) {
        this.colony = game;
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton continueButton = new TextButton("Continue", skin);
        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton loadGameButton = new TextButton("Load Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

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

        table.add(continueButton).row();
        table.add(newGameButton).row();
        table.add(loadGameButton).row();
        table.add(exitButton).row();

        Gdx.input.setInputProcessor(stage);

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

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(final float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
