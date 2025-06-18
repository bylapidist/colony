package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Base class for simple UI screens.
 */
public abstract class BaseScreen extends ScreenAdapter {

    private final Stage stage;
    private final Skin skin;
    private final Table root;
    private static final float PADDING = 10f;

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
        root.center();
        root.defaults().pad(PADDING);
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

    /**
     * Creates a text button using the active UI skin.
     *
     * @param text button label
     * @return newly created button
     */
    protected final TextButton createButton(final String text) {
        return new TextButton(text, skin);
    }

    /**
     * Creates a label using the active UI skin.
     *
     * @param text label contents
     * @return newly created label
     */
    protected final Label createLabel(final String text) {
        return new Label(text, skin);
    }

    /**
     * Creates a dialog using the "dialog" style of the active UI skin.
     *
     * @param title dialog title
     * @return newly created dialog
     */
    protected final Dialog createDialog(final String title) {
        return new Dialog(title, skin, "dialog");
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
