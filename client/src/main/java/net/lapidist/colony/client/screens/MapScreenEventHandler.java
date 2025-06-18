package net.lapidist.colony.client.screens;

import net.lapidist.colony.client.events.DisposeEvent;
import net.lapidist.colony.client.events.HideEvent;
import net.lapidist.colony.client.events.PauseEvent;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.client.events.ResumeEvent;
import net.lapidist.colony.client.events.ShowEvent;
import net.lapidist.colony.events.Events;

/**
 * Handles lifecycle events for {@link MapScreen}.
 */
public final class MapScreenEventHandler {

    private static final float HALF_SPEED = 0.5f;

    private MapScreen screen;

    /** Attach the screen this handler controls. */
    public void attach(final MapScreen target) {
        this.screen = target;
    }

    public void update() {
        Events.update();
    }

    public void resize(final int width, final int height) {
        Events.dispatch(new ResizeEvent(width, height));
    }

    public void pause() {
        if (screen != null) {
            screen.setPaused(true);
        }
        Events.dispatch(new PauseEvent());
    }

    public void resume() {
        if (screen != null) {
            screen.setPaused(false);
        }
        Events.dispatch(new ResumeEvent());
    }

    public void hide() {
        Events.dispatch(new HideEvent());
    }

    public void show() {
        Events.dispatch(new ShowEvent());
    }

    public void dispose() {
        Events.dispatch(new DisposeEvent());
    }

    /** Toggle paused state and dispatch corresponding event. */
    public void togglePause() {
        if (screen == null) {
            return;
        }
        if (screen.isPaused()) {
            resume();
        } else {
            pause();
        }
    }

    /** Enable or disable slow motion at half speed. */
    public void setSlowMotion(final boolean enabled) {
        if (screen != null) {
            screen.setSpeedMultiplier(enabled ? HALF_SPEED : 1f);
        }
    }
}
