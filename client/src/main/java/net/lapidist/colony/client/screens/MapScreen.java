package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.MapStateProvider;

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
    private java.util.function.Consumer<Float> progressCallback;
    private net.lapidist.colony.client.systems.MapInitSystem initSystem;
    private static final float DEFAULT_SCALE = 1f;

    private void applyScale() {
        float scale = colony.getSettings() == null ? DEFAULT_SCALE : colony.getSettings().getUiScale();
        ScreenViewport viewport = (ScreenViewport) stage.getViewport();
        viewport.setUnitsPerPixel(1f / scale);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public MapScreen(final Colony colonyToSet, final MapState state, final GameClient client) {
        this.colony = colonyToSet;
        stage = new Stage(new ScreenViewport());
        applyScale();
        world = MapWorldBuilder.build(
                MapWorldBuilder.builder(
                        state,
                        client,
                        stage,
                        colony.getSettings().getKeyBindings(),
                        colony.getSettings().getGraphicsSettings()
                ),
                null,
                colony.getSettings(),
                state.cameraPos(),
                client,
                state.playerResources(),
                state.playerPos()
        );
        var cameraSystem = world.getSystem(net.lapidist.colony.client.systems.PlayerCameraSystem.class);
        if (cameraSystem != null) {
            cameraSystem.setClient(client);
        }
        MapUi ui = MapUiBuilder.build(stage, world, client, colony);
        minimapActor = ui.getMinimapActor();
        events = new MapScreenEventHandler();
        initSystem = world.getSystem(net.lapidist.colony.client.systems.MapInitSystem.class);
        progressCallback = null;
    }

    public MapScreen(
            final Colony colonyToSet,
            final MapStateProvider provider,
            final GameClient client,
            final java.util.function.Consumer<Float> progress
    ) {
        this.colony = colonyToSet;
        stage = new Stage(new ScreenViewport());
        applyScale();
        world = MapWorldBuilder.build(
                MapWorldBuilder.builder(
                        provider,
                        client,
                        stage,
                        colony.getSettings().getKeyBindings(),
                        colony.getSettings().getGraphicsSettings(),
                        progress
                ),
                null,
                colony.getSettings(),
                null,
                client,
                new net.lapidist.colony.components.state.ResourceData(),
                null
        );
        var cameraSystem = world.getSystem(net.lapidist.colony.client.systems.PlayerCameraSystem.class);
        if (cameraSystem != null) {
            cameraSystem.setClient(client);
        }
        MapUi ui = MapUiBuilder.build(stage, world, client, colony);
        minimapActor = ui.getMinimapActor();
        events = new MapScreenEventHandler();
        progressCallback = progress;
        initSystem = world.getSystem(net.lapidist.colony.client.systems.MapInitSystem.class);
        if (initSystem != null && progressCallback != null) {
            progressCallback.accept(0f);
        }
    }

    @Override
    public void render(final float deltaTime) {
        if (initSystem != null && progressCallback != null && !initSystem.isReady()) {
            progressCallback.accept(initSystem.getProgress());
        }
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
        applyScale();
        events.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {
        events.hide();
    }

    @Override
    public void show() {
        applyScale();
        events.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
