package net.lapidist.colony.client.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.MapGenerationSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.UISystem;

import java.io.IOException;

public class MapScreen implements Screen {

    private final PooledEngine pooledEngine = new PooledEngine();
    private final ResourceLoader resourceLoader = new ResourceLoader();

    public MapScreen() {
        try {
            resourceLoader.load(
                    FileLocation.INTERNAL,
                    "resources.json"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        pooledEngine.addSystem(new ClearScreenSystem(Color.BLACK));
        pooledEngine.addSystem(new MapGenerationSystem(Constants.MAP_WIDTH, Constants.MAP_HEIGHT));
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
        resourceLoader.dispose();
        Events.dispatch(EventType.DISPOSE);
    }
}
