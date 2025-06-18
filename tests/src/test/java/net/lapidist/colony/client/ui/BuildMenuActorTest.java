package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
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
    public void hoveringButtonShowsTooltip() {
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
        Stage stage = new Stage(new ScreenViewport(), org.mockito.Mockito.mock(Batch.class));
        stage.addActor(menu);
        menu.setVisible(true);
        Button button = menu.findActor("farm");
        assertNotNull(button);
        TextTooltip tooltip = null;
        for (com.badlogic.gdx.scenes.scene2d.EventListener l : button.getListeners()) {
            if (l instanceof TextTooltip tt) {
                tooltip = tt;
            }
        }
        assertNotNull(tooltip);
        TooltipManager.getInstance().instant();
        InputEvent enter = new InputEvent();
        enter.setType(InputEvent.Type.enter);
        enter.setStage(stage);
        enter.setListenerActor(button);
        button.fire(enter);
        final float actTime = 0.1f;
        stage.act(actTime);
        assertTrue(tooltip.getContainer().isVisible());
    }
}
