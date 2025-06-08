package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.MapState;

/**
 * Gameplay screen that delegates world setup, UI creation and event handling
 * to helper classes.
 */
public final class MapScreen implements Screen {

    private final Colony colony;
    private final World world;
    private final Stage stage;
    private final MinimapActor minimapActor;
    private final MapScreenEventHandler events;

    public MapScreen(final Colony colonyToSet, final MapState state, final GameClient client) {
        this.colony = colonyToSet;
        stage = new Stage(new ScreenViewport());
        world = MapWorldBuilder.build(state, client, stage);
        MapUi ui = MapUiBuilder.build(stage, world, colony);
        minimapActor = ui.getMinimapActor();
        events = new MapScreenEventHandler();
    }

    @Override
    public void render(final float deltaTime) {
        events.update();
        world.setDelta(deltaTime);
        world.process();
    }

    @Override
    public void resize(final int width, final int height) {
        events.resize(width, height);
    }

    @Override
    public void pause() {
        events.pause();
    }

    @Override
    public void resume() {
        events.resume();
    }

    @Override
    public void hide() {
        events.hide();
    }

    @Override
    public void show() {
        events.show();
    }

    @Override
    public void dispose() {
        events.dispose();
        world.dispose();
        minimapActor.dispose();
        stage.dispose();
    }
}
