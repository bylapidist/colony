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

        table.add(continueButton).row();
        table.add(newGameButton).row();
        table.add(loadGameButton).row();
        table.add(exitButton).row();

        Gdx.input.setInputProcessor(stage);

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.startGame();
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                Gdx.app.exit();
            }
        });
        // Additional button functionality can be implemented here
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
