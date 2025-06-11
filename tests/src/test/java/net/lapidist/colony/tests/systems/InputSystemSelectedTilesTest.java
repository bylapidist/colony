package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class InputSystemSelectedTilesTest {
    private static final int INITIAL_WOOD = 5;

    @Test
    public void gatherKeyUsesSelectedTiles() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(INITIAL_WOOD, 0, 0);
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .selected(true)
                .resources(res)
                .build());
        state.tiles().put(new TilePos(1, 0), TileData.builder()
                .x(1).y(0).tileType("GRASS").passable(true)
                .resources(res)
                .build());

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        InputSystem system = new InputSystem(client, keys);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        new CameraInputSystem(keys), system)
                .build());

        Input input = mock(Input.class);
        Gdx.input = input;
        when(input.isKeyJustPressed(keys.getKey(KeyAction.GATHER)))
                .thenReturn(false);

        world.process();

        when(input.isKeyJustPressed(keys.getKey(KeyAction.GATHER)))
                .thenReturn(true);
        world.process();

        ArgumentCaptor<net.lapidist.colony.components.state.ResourceGatherRequestData> captor =
                ArgumentCaptor.forClass(net.lapidist.colony.components.state.ResourceGatherRequestData.class);
        verify(client).sendGatherRequest(captor.capture());
        assertEquals(0, captor.getValue().x());
        assertEquals(0, captor.getValue().y());
    }
}
