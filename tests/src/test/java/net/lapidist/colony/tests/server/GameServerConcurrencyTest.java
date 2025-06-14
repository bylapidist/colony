package net.lapidist.colony.tests.server;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class GameServerConcurrencyTest {

    private static final int WAIT_MS = 200;
    private static final int AUTOSAVE_MS = 10;

    @Test
    public void autosaveRunsWhileProcessingCommands() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(1, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("concurrency")
                .autosaveInterval(AUTOSAVE_MS)
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("concurrency");
        GameServer server = new GameServer(config);
        server.start();

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        client.sendBuildRequest(new BuildingPlacementData(0, 0, "HOUSE"));
        Thread.sleep(WAIT_MS);

        assertTrue(server.getMapState().buildings().stream()
                .anyMatch(b -> b.x() == 0 && b.y() == 0));
        assertTrue(java.nio.file.Files.exists(
                net.lapidist.colony.io.Paths.get().getAutosave("concurrency")));

        client.stop();
        server.stop();
        Thread.sleep(WAIT_MS);
    }
}
