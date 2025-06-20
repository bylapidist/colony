package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.util.I18n;

/**
 * Simple loading screen with a progress bar.
 */
public final class LoadingScreen extends BaseScreen {

    private static final float STEP = 0.01f;
    private static final float PADDING = 5f;
    private static final float BAR_WIDTH = 200f;

    private final Label messageLabel;
    private final ProgressBar progressBar;

    public LoadingScreen() {
        this(1f);
    }

    public LoadingScreen(final float scale) {
        getStage().getRoot().setScale(scale);
        messageLabel = new Label(I18n.get("loading.title"), getSkin());
        ProgressBarStyle style = new ProgressBarStyle();
        style.background = getSkin().newDrawable("white_pixel", Color.DARK_GRAY);
        style.knobBefore = getSkin().newDrawable("white_pixel", Color.GREEN);
        style.knob = getSkin().newDrawable("white_pixel", Color.GREEN);
        progressBar = new ProgressBar(0f, 1f, STEP, false, style);
        progressBar.setValue(0f);

        getRoot().add(messageLabel).padBottom(PADDING).row();
        getRoot().add(progressBar).width(BAR_WIDTH).row();
    }

    /**
     * Update the progress bar value.
     *
     * @param progress value between 0 and 1
     */
    public void setProgress(final float progress) {
        progressBar.setValue(Math.max(0f, Math.min(1f, progress)));
    }

    /**
     * Update the displayed loading message.
     */
    public void setMessage(final String message) {
        messageLabel.setText(message);
    }
}
