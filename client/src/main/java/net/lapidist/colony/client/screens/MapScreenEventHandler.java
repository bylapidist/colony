package net.lapidist.colony.client.screens;

import net.lapidist.colony.client.events.DisposeEvent;
import net.lapidist.colony.client.events.HideEvent;
import net.lapidist.colony.client.events.PauseEvent;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.client.events.ResumeEvent;
import net.lapidist.colony.client.events.ShowEvent;
import net.lapidist.colony.core.events.Events;

/**
 * Handles lifecycle events for {@link MapScreen}.
 */
public final class MapScreenEventHandler {

    public void update() {
        Events.update();
    }

    public void resize(final int width, final int height) {
        Events.dispatch(new ResizeEvent(width, height));
    }

    public void pause() {
        Events.dispatch(new PauseEvent());
    }

    public void resume() {
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
}
