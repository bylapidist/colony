package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Basic tests for {@link BuildMenuActor}. */
@RunWith(GdxTestRunner.class)
public class BuildMenuActorTest {

    @Test
    public void selectingButtonUpdatesSystem() {
        new BaseDefinitionsMod().init();
        BuildPlacementSystem system = new BuildPlacementSystem(null, new KeyBindings());
        com.artemis.World world = new com.artemis.World(new com.artemis.WorldConfigurationBuilder()
                .with(new net.lapidist.colony.client.systems.PlayerCameraSystem(),
                        new net.lapidist.colony.client.systems.CameraInputSystem(new KeyBindings()),
                        system)
                .build());
        world.process();

        Skin skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/default.json"));
        BuildMenuActor menu = new BuildMenuActor(skin, system, new GraphicsSettings());
        Stage stage = new Stage(
                new ScreenViewport(),
                org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.Batch.class)
        );
        stage.addActor(menu);
        menu.setVisible(true);
        com.badlogic.gdx.scenes.scene2d.ui.Button button = menu.findActor("farm");
        assertNotNull(button);
        button.toggle();
        assertEquals("farm", system.getSelectedBuilding());
    }

    @Test
    public void displaysResourceIcons() {
        new BaseDefinitionsMod().init();
        BuildPlacementSystem system = new BuildPlacementSystem(null, new KeyBindings());
        com.artemis.World world = new com.artemis.World(new com.artemis.WorldConfigurationBuilder()
                .with(new net.lapidist.colony.client.systems.PlayerCameraSystem(),
                        new net.lapidist.colony.client.systems.CameraInputSystem(new KeyBindings()),
                        system)
                .build());
        world.process();

        Skin skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/default.json"));
        BuildMenuActor menu = new BuildMenuActor(skin, system, new GraphicsSettings());
        Stage stage = new Stage(
                new ScreenViewport(),
                org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.Batch.class)
        );
        stage.addActor(menu);
        menu.setVisible(true);
        com.badlogic.gdx.scenes.scene2d.ui.Button button = menu.findActor("market");
        assertNotNull(button);
        assertNotNull(button.findActor("woodIcon"));
        assertNotNull(button.findActor("stoneIcon"));
        assertNull(button.findActor("foodIcon"));
    }

    @Test
    public void tooltipAppearsOnHover() {
        new BaseDefinitionsMod().init();
        BuildPlacementSystem system = new BuildPlacementSystem(null, new KeyBindings());
        com.artemis.World world = new com.artemis.World(new com.artemis.WorldConfigurationBuilder()
                .with(new net.lapidist.colony.client.systems.PlayerCameraSystem(),
                        new net.lapidist.colony.client.systems.CameraInputSystem(new KeyBindings()),
                        system)
                .build());
        world.process();

        Skin skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/default.json"));
        BuildMenuActor menu = new BuildMenuActor(skin, system, new GraphicsSettings());
        Stage stage = new Stage(
                new ScreenViewport(),
                org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.Batch.class)
        );
        stage.addActor(menu);
        menu.setVisible(true);
        com.badlogic.gdx.scenes.scene2d.ui.Button button = menu.findActor("farm");
        assertNotNull(button);
        com.badlogic.gdx.scenes.scene2d.ui.Tooltip<?> tooltip = null;
        for (com.badlogic.gdx.scenes.scene2d.EventListener l : button.getListeners()) {
            if (l instanceof com.badlogic.gdx.scenes.scene2d.ui.Tooltip<?> t) {
                tooltip = t;
                break;
            }
        }
        assertNotNull(tooltip);
        com.badlogic.gdx.scenes.scene2d.InputEvent event = new com.badlogic.gdx.scenes.scene2d.InputEvent();
        event.setListenerActor(button);
        ((com.badlogic.gdx.scenes.scene2d.ui.Tooltip<?>) tooltip).enter(event, 0f, 0f, -1, null);
        stage.act(0f);
        assertTrue(((com.badlogic.gdx.scenes.scene2d.ui.Tooltip<?>) tooltip).getContainer().hasParent());
    }
}
