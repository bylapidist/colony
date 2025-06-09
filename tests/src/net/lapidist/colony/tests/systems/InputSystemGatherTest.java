package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class InputSystemGatherTest {

    @Test
    public void sendsGatherRequestUsingConfiguredKey() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        GameClient client = mock(GameClient.class);
        Settings settings = new Settings();
        settings.setKey(KeyAction.GATHER, Input.Keys.L);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new InputSystem(client, settings))
                .build());

        world.process();
        // Mark tile as selected
        com.artemis.utils.IntBag maps = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(net.lapidist.colony.components.maps.MapComponent.class))
                .getEntities();
        com.artemis.Entity map = world.getEntity(maps.get(0));
        net.lapidist.colony.components.maps.MapComponent mapComponent =
                world.getMapper(net.lapidist.colony.components.maps.MapComponent.class).get(map);
        TileComponent tc =
                world.getMapper(TileComponent.class).get(mapComponent.getTiles().get(0));
        tc.setSelected(true);

        com.badlogic.gdx.Input input = mock(com.badlogic.gdx.Input.class);
        com.badlogic.gdx.Gdx.input = input;
        when(input.isKeyJustPressed(Input.Keys.L)).thenReturn(true);

        world.setDelta(0f);
        world.process();

        verify(client).sendGatherRequest(any());
    }
}
