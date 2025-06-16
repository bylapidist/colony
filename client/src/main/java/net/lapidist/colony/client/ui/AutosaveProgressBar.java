package net.lapidist.colony.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.events.autosave.AutosaveEvent;
import net.lapidist.colony.events.autosave.AutosaveStartEvent;
import net.mostlyoriginal.api.event.common.Subscribe;

/** Progress bar displayed while the game state is being autosaved. */
public final class AutosaveProgressBar extends ProgressBar {
    private static final float STEP = 0.01f;
    private static final float DISPLAY_TIME = 2f;
    private float timer;
    private boolean running;

    public AutosaveProgressBar(final Skin skin) {
        super(0f, 1f, STEP, false, createStyle(skin));
        setName("autosaveProgress");
        setVisible(false);
        if (Events.getInstance() != null) {
            Events.getInstance().registerEvents(this);
        }
    }

    private static ProgressBarStyle createStyle(final Skin skin) {
        ProgressBarStyle style = new ProgressBarStyle();
        style.background = skin.newDrawable("white_pixel", Color.DARK_GRAY);
        style.knobBefore = skin.newDrawable("white_pixel", Color.GREEN);
        style.knob = skin.newDrawable("white_pixel", Color.GREEN);
        return style;
    }

    @Subscribe
    private void onAutosaveStart(final AutosaveStartEvent event) {
        setValue(0f);
        timer = 0f;
        running = true;
        setVisible(true);
    }

    @Subscribe
    private void onAutosaveComplete(final AutosaveEvent event) {
        setValue(1f);
        timer = DISPLAY_TIME;
        running = false;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (!isVisible()) {
            return;
        }
        if (running) {
            setValue(Math.min(1f, getValue() + delta));
        } else {
            timer -= delta;
            if (timer <= 0f) {
                setVisible(false);
            }
        }
    }
}
