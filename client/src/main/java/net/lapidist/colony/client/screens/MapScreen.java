package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.map.MapState;

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
    /** True when world updates are paused. */
    private boolean paused;
    /** Multiplier applied to frame time for slow or fast motion. */
    private float speedMultiplier;
    private static final float DEFAULT_SCALE = 1f;
    /** Fixed time step used for deterministic updates. */
    private static final double STEP_TIME = 1d / 60d;
    /**
     * Accumulates frame time so the world can be stepped in fixed increments.
     */
    private double accumulator;

    private void applyScale() {
        float scale = colony.getSettings() == null ? DEFAULT_SCALE : colony.getSettings().getUiScale();
        ScreenViewport viewport = (ScreenViewport) stage.getViewport();
        viewport.setUnitsPerPixel(1f / scale);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public MapScreen(final Colony colonyToSet, final MapState state, final GameClient client) {
        this(colonyToSet, state, client, null);
    }

    public MapScreen(
            final Colony colonyToSet,
            final MapState state,
            final GameClient client,
            final java.util.function.Consumer<Float> callback
    ) {
        this.colony = colonyToSet;
        stage = new Stage(new ScreenViewport());
        applyScale();
        world = MapWorldBuilder.build(
                MapWorldBuilder.builder(
                        state,
                        client,
                        stage,
                        colony.getSettings().getKeyBindings(),
                        colony.getSettings().getGraphicsSettings(),
                        callback
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
        events = new MapScreenEventHandler();
        MapUi ui = MapUiBuilder.build(stage, world, client, colony, events);
        minimapActor = ui.getMinimapActor();
        events.attach(this);
        accumulator = 0.0;
        paused = false;
        speedMultiplier = 1f;
    }

    /**
     * Pause or resume world updates.
     */
    public void setPaused(final boolean value) {
        this.paused = value;
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Adjust the rate at which accumulated time advances.
     */
    public void setSpeedMultiplier(final float multiplier) {
        this.speedMultiplier = multiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    @Override
    public void render(final float deltaTime) {
        events.update();
        if (!paused) {
            accumulator += deltaTime * speedMultiplier;
        }
        MapInitSystem init = world.getSystem(MapInitSystem.class);
        boolean loading = init != null && !init.isReady();
        while (!paused && accumulator >= STEP_TIME) {
            world.setDelta((float) STEP_TIME);
            world.process();
            accumulator -= STEP_TIME;
            if (loading) {
                break;
            }
        }
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
