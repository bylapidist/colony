package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class InputSystemTest {

    @Test
    public void tapSelectsTileUnderCursor() {
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
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new InputSystem(client, settings))
                .build());

        world.process();

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        camera.getCamera().position.set(Constants.TILE_SIZE / 2f, Constants.TILE_SIZE / 2f, 0);
        camera.getCamera().update();

        Vector2 screenCoords = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        float tapX = screenCoords.x;
        float tapY = screenCoords.y;

        InputSystem input = world.getSystem(InputSystem.class);
        input.tap(tapX, tapY, 1, 0);

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity map = world.getEntity(maps.get(0));
        MapComponent mapComponent = world.getMapper(MapComponent.class).get(map);
        TileComponent tileComponent = world.getMapper(TileComponent.class)
                .get(mapComponent.getTiles().get(0));

        assertFalse(tileComponent.isSelected());
        verify(client).sendTileSelectionRequest(any());
    }
}
