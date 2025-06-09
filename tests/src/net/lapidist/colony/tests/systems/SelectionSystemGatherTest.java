package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/** Tests resource gathering via mouse input. */
@RunWith(GdxTestRunner.class)
public class SelectionSystemGatherTest {
    private static final int INITIAL_WOOD = 5;

    @Test
    public void tapSendsGatherRequest() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(INITIAL_WOOD, 0, 0);
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .resources(res)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        GameClient client = mock(GameClient.class);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        new SelectionSystem(client, new net.lapidist.colony.settings.KeyBindings()))
                .build());

        world.process();

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        camera.getCamera().position.set(Constants.TILE_SIZE / 2f, Constants.TILE_SIZE / 2f, 0);
        camera.getCamera().update();

        Vector2 screenCoords = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        SelectionSystem input = world.getSystem(SelectionSystem.class);
        input.tap(screenCoords.x, screenCoords.y, 1, 0);

        verify(client).sendGatherRequest(any());
    }
}
