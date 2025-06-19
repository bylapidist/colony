package net.lapidist.colony.tests.screens;

import com.artemis.World;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.LogicWorldBuilder;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class LogicWorldBuilderTest {

    @Test
    public void baseBuilderRegistersLogicSystems() {
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        World world = LogicWorldBuilder.build(
                LogicWorldBuilder.baseBuilder(client, keys),
                client,
                new net.lapidist.colony.components.state.resources.ResourceData(),
                null
        );
        assertNotNull(world.getSystem(CameraInputSystem.class));
        assertNotNull(world.getSystem(SelectionSystem.class));
        assertNotNull(world.getSystem(BuildPlacementSystem.class));
        assertNotNull(world.getSystem(PlayerMovementSystem.class));
        world.dispose();
    }

    @Test
    public void builderAddsMapInitSystem() {
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        MapState state = new MapState();
        World world = LogicWorldBuilder.build(
                LogicWorldBuilder.builder(state, client, keys, null),
                client,
                state.playerResources(),
                state.playerPos()
        );
        assertNotNull(world.getSystem(net.lapidist.colony.client.systems.MapInitSystem.class));
        assertNotNull(world.getSystem(TileUpdateSystem.class));
        world.dispose();
    }
}
