package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.lapidist.colony.client.events.Events;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.client.events.PauseEvent;
import net.lapidist.colony.client.events.ResumeEvent;
import net.lapidist.colony.client.events.HideEvent;
import net.lapidist.colony.client.events.ShowEvent;
import net.lapidist.colony.client.events.DisposeEvent;

public class MapScreen implements Screen {

    private final World world;

    public MapScreen(final MapState state) {
        world = new World(new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        new ClearScreenSystem(Color.BLACK),
                        new MapLoadSystem(state),
                        new PlayerCameraSystem(),
                        new InputSystem(),
                        new MapRenderSystem(),
                        new UISystem()
                )
                .build());
        Events.init(world.getSystem(EventSystem.class));
    }

    @Override
    public final void render(final float deltaTime) {
        Events.update();
        world.setDelta(deltaTime);
        world.process();
    }

    @Override
    public final void resize(final int width, final int height) {
        Events.dispatch(new ResizeEvent(width, height));
    }

    @Override
    public final void pause() {
        Events.dispatch(new PauseEvent());
    }

    @Override
    public final void resume() {
        Events.dispatch(new ResumeEvent());
    }

    @Override
    public final void hide() {
        Events.dispatch(new HideEvent());
    }

    @Override
    public final void show() {
        Events.dispatch(new ShowEvent());
    }

    @Override
    public final void dispose() {
        Events.dispatch(new DisposeEvent());
        world.dispose();
    }
}
