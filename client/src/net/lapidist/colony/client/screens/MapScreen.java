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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.components.state.MapState;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.client.events.PauseEvent;
import net.lapidist.colony.client.events.ResumeEvent;
import net.lapidist.colony.client.events.HideEvent;
import net.lapidist.colony.client.events.ShowEvent;
import net.lapidist.colony.client.events.DisposeEvent;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.i18n.I18n;

public class MapScreen implements Screen {

    private final Colony colony;

    private final World world;
    private final Stage stage;
    private final MinimapActor minimapActor;
    private static final float PADDING = 10f;

    public MapScreen(final Colony colonyToSet, final MapState state, final GameClient client) {
        this.colony = colonyToSet;
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));

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

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        stage.addActor(table);

        TextButton menuButton = new TextButton(I18n.get("map.menu"), skin);
        minimapActor = new MinimapActor(world);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.returnToMainMenu();
            }
        });

        table.add(menuButton).pad(PADDING).left().top();
        table.add(minimapActor).pad(PADDING).expandX().right().top();
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
        minimapActor.dispose();
        stage.dispose();
    }
}
