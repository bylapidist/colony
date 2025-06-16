package net.lapidist.colony.tests.client.input;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class BuildingPlacementHandlerTest {

    @Test
    public void tapOnTileSendsBuildRequest() {
        GameClient client = mock(GameClient.class);
        PlayerCameraSystem cameraSystem = new PlayerCameraSystem();
        BuildingPlacementHandler handler = new BuildingPlacementHandler(client, cameraSystem);

        World world = new World(new WorldConfigurationBuilder().build());
        Entity tile = world.createEntity();
        TileComponent tc = new TileComponent();
        tc.setX(0);
        tc.setY(0);
        tile.edit().add(tc);

        java.util.Map<TilePos, Entity> tileMap = new HashMap<>();
        tileMap.put(new TilePos(0, 0), tile);

        MapComponent map = mock(MapComponent.class);
        when(map.getTileMap()).thenReturn(tileMap);

        ((OrthographicCamera) cameraSystem.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0f
        );
        cameraSystem.getCamera().update();

        handler.setBuildingId("farm");
        Vector2 screen = CameraUtils.worldToScreenCoords(cameraSystem.getViewport(), 0, 0);
        boolean handled = handler.handleTap(screen.x, screen.y, map, world.getMapper(TileComponent.class));

        assertTrue(handled);
        ArgumentCaptor<net.lapidist.colony.components.state.BuildingPlacementData> captor =
                ArgumentCaptor.forClass(net.lapidist.colony.components.state.BuildingPlacementData.class);
        verify(client).sendBuildRequest(captor.capture());
        org.junit.Assert.assertEquals("farm", captor.getValue().buildingId());
        world.dispose();
    }
}
