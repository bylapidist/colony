package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public class MapScreen implements Screen {

    private final World world;

    public MapScreen(final MapState state) {
        world = new World(new WorldConfigurationBuilder()
                .with(
                        new ClearScreenSystem(Color.BLACK),
                        new MapLoadSystem(state),
                        new PlayerCameraSystem(),
                        new InputSystem(),
                        new MapRenderSystem(),
                        new UISystem()
                )
                .build());
    }

    @Override
    public final void render(final float deltaTime) {
        Events.update();
        world.setDelta(deltaTime);
        world.process();
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
        world.dispose();
    }
}
