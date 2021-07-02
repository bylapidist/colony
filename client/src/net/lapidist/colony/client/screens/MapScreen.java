package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.core.events.EventType;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.payloads.ResizePayload;

public class MapScreen implements Screen {

    private final ClearScreenSystem clearScreenSystem;

    public MapScreen() {
        clearScreenSystem = new ClearScreenSystem(Color.BLACK);
    }

    @Override
    public final void render(final float delta) {
        Events.update();
        this.clearScreenSystem.update();
    }

    @Override
    public final void resize(final int width, final int height) {
        Events.dispatch(EventType.RESIZE, new ResizePayload(width, height));
    }

    @Override
    public final void pause() {
        Events.dispatch(EventType.PAUSE);
    }

    @Override
    public final void resume() {
        Events.dispatch(EventType.RESUME);
    }

    @Override
    public final void hide() {
        Events.dispatch(EventType.HIDE);
    }

    @Override
    public final void show() {
        Events.dispatch(EventType.SHOW);
    }

    @Override
    public final void dispose() {
        Events.dispatch(EventType.DISPOSE);
    }
}
