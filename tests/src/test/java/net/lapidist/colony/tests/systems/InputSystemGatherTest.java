package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/** Tests resource gathering via mouse input. */
@RunWith(GdxTestRunner.class)
public class InputSystemGatherTest {
    private static final int INITIAL_WOOD = 5;

    @Test
    public void tapSendsGatherRequest() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(INITIAL_WOOD, 0, 0);
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .resources(res)
                .build();
        state.putTile(tile);

        GameClient client = mock(GameClient.class);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        new CameraInputSystem(new net.lapidist.colony.settings.KeyBindings()),
                        new MapRenderDataSystem(),
                        new SelectionSystem(client, new net.lapidist.colony.settings.KeyBindings()))
                .build());

        world.process();

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0
        );
        camera.getCamera().update();

        Vector2 screenCoords = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        SelectionSystem input = world.getSystem(SelectionSystem.class);
        input.setSelectMode(true);
        input.tap(screenCoords.x, screenCoords.y);

        verify(client).sendGatherRequest(any());
    }
}
