package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class InputSystemGatherTest {
    private static final float DELTA = 1f / 60f;
    @Test
    public void sendGatherRequestWhenKeyPressed() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .selected(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        GameClient client = mock(GameClient.class);
        Settings settings = new Settings();
        settings.setKey(Settings.Action.GATHER, Input.Keys.G);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new InputSystem(client, settings))
                .build());
        world.process();

        com.badlogic.gdx.Input input = mock(com.badlogic.gdx.Input.class);
        when(input.isKeyJustPressed(Input.Keys.G)).thenReturn(true);
        Gdx.input = input;

        world.setDelta(DELTA);
        world.process();

        verify(client).sendGatherRequest(any());
        world.dispose();
    }
}
