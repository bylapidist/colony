package net.lapidist.colony.tests.screens;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.LogicWorldBuilder;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class LogicWorldBuilderConfigurationTest {

    @Test
    public void baseBuilderRegistersLogicSystems() {
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = LogicWorldBuilder.build(
                    LogicWorldBuilder.baseBuilder(client, stage, keys),
                    client,
                    null,
                    new net.lapidist.colony.components.state.ResourceData(),
                    null
            );
            assertNotNull(world.getSystem(CameraInputSystem.class));
            assertNotNull(world.getSystem(SelectionSystem.class));
            assertNotNull(world.getSystem(BuildPlacementSystem.class));
            assertNotNull(world.getSystem(PlayerMovementSystem.class));
            assertNotNull(world.getSystem(TileUpdateSystem.class));
            assertNotNull(world.getSystem(ChunkLoadSystem.class));
            world.dispose();
        }
    }
}
