package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Base class for simple UI screens.
 */
public abstract class BaseScreen extends ScreenAdapter {

    private final Stage stage;
    private final Skin skin;
    private final Table root;

    /**
     * Creates a screen with a default {@link Stage} instance.
     */
    protected BaseScreen() {
        this(new Stage(new ScreenViewport()));
    }

    /**
     * Creates a screen using the provided stage.
     *
     * @param customStage stage used to display the UI
     */
    protected BaseScreen(final Stage customStage) {
        stage = customStage;
        skin = new Skin(Gdx.files.internal("skin/default.json"));
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Returns the stage used for rendering.
     *
     * @return the stage used for rendering
     */
    protected final Stage getStage() {
        return stage;
    }

    /**
     * Returns the skin used for UI elements.
     *
     * @return the UI skin in use
     */
    protected final Skin getSkin() {
        return skin;
    }

    /**
     * Returns the root table containing all actors.
     *
     * @return the root table containing all actors
     */
    protected final Table getRoot() {
        return root;
    }

    @Override
    public final void render(final float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public final void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public final void dispose() {
        stage.dispose();
    }
}
