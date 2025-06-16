package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class BuildPlacementWithSelectionSystemTest {

    @Test
    public void tapPlacesBuildingWhenSelectionSystemPresent() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build();
        state.putTile(tile);

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        CameraInputSystem cameraInput = new CameraInputSystem(keys);
        SelectionSystem selectionSystem = new SelectionSystem(client, keys);
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), cameraInput,
                        selectionSystem, buildSystem)
                .build());
        world.process();

        buildSystem.setBuildMode(true);
        buildSystem.setSelectedBuilding("farm");

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0f
        );
        camera.getCamera().update();

        Vector2 screen = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        buildSystem.tap(screen.x, screen.y);

        org.mockito.ArgumentCaptor<net.lapidist.colony.components.state.BuildingPlacementData> captor =
                org.mockito.ArgumentCaptor.forClass(net.lapidist.colony.components.state.BuildingPlacementData.class);
        verify(client).sendBuildRequest(captor.capture());
        assertEquals("farm", captor.getValue().buildingId());
        assertFalse(world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(MapUtils.findMap(world).get().getTiles().get(0)).isSelected());
    }
}
