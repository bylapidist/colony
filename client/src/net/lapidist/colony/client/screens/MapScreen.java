package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.network.GameClient;
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
    private final Stage stage;
    private static final float PADDING = 10f;

    public MapScreen(final MapState state, final GameClient client) {
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        Table table = new Table();
        table.setFillParent(true);
        table.top().left();
        stage.addActor(table);
        TextButton menuButton = new TextButton("Menu", skin);
        table.add(menuButton).pad(PADDING);

        InputSystem inputSystem = new InputSystem(client);
        inputSystem.addProcessor(stage);

        world = new World(new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        new ClearScreenSystem(Color.BLACK),
                        new MapLoadSystem(state),
                        new PlayerCameraSystem(),
                        inputSystem,
                        new MapRenderSystem(),
                        new UISystem(stage)
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
        stage.dispose();
    }
}
