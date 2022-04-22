package net.lapidist.colony.client.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public class MapScreen implements Screen {

    private final PooledEngine pooledEngine = new PooledEngine();

    public MapScreen() {
        pooledEngine.addSystem(new ClearScreenSystem(Color.BLACK));
        pooledEngine.addSystem(new MapGenerationSystem(Constants.MAP_WIDTH, Constants.MAP_HEIGHT));
        pooledEngine.addSystem(new PlayerCameraSystem());
        pooledEngine.addSystem(new InputSystem());
        pooledEngine.addSystem(new MapRenderSystem());
        pooledEngine.addSystem(new UISystem());
    }

    @Override
    public final void render(final float deltaTime) {
        Events.update();
        pooledEngine.update(deltaTime);
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
