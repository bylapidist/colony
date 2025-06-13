package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.components.state.MapChunkRequest;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class ChunkLoadSystemTest {

    @Test
    public void requestsMissingChunksNearCamera() {
        MapState state = new MapState();
        GameClient client = Mockito.mock(GameClient.class);
        Mockito.when(client.getMapState()).thenReturn(state);

        ChunkLoadSystem system = new ChunkLoadSystem(client);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), system)
                .build());
        world.process();

        // initial request for the player's starting chunk
        verify(client, Mockito.atLeastOnce()).send(Mockito.eq(new MapChunkRequest(0, 0)));

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        final float offset = 10f;
        ((OrthographicCamera) camera.getCamera()).position.set(
                MapChunkData.CHUNK_SIZE * GameConstants.TILE_SIZE + offset,
                GameConstants.TILE_SIZE,
                0f);
        camera.getCamera().update();

        world.process();

        verify(client, Mockito.atLeastOnce()).send(Mockito.eq(new MapChunkRequest(1, 0)));
    }
}
