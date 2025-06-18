package net.lapidist.colony.tests.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class TileSelectionHandlerSelectedTilesTest {

    @Test
    public void maintainsSelectedTileList() {
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        GameClient client = mock(GameClient.class);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new MapRenderDataSystem())
                .build());
        world.process();

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0
        );
        camera.getCamera().update();

        Array<Entity> selected = new Array<>();
        TileSelectionHandler handler = new TileSelectionHandler(
                client,
                camera,
                selected,
                world.getSystem(MapRenderDataSystem.class)
        );
        MapComponent map = MapUtils.findMap(world).orElseThrow();
        var tileMapper = world.getMapper(TileComponent.class);

        Vector2 screen = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        handler.handleTap(screen.x, screen.y, map, tileMapper);
        assertEquals(1, selected.size);

        // simulate server update
        var tile = map.getTiles().first();
        tileMapper.get(tile).setSelected(true);

        handler.handleTap(screen.x, screen.y, map, tileMapper);
        assertEquals(0, selected.size);
        verify(client, times(2)).sendTileSelectionRequest(any());
    }
}
