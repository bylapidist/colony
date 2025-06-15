package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapWorldBuilder;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.state.CameraPosition;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class GameSimulationSpawnNetworkTest {

    @Test
    public void playerAndCameraSpawnFromMetadata() throws Exception {
        final String save = "spawn-net";
        final int px = 2;
        final int py = 3;
        final float camX = 4f;
        final float camY = 5f;
        MapState initial = new MapState().toBuilder()
                .playerPos(new PlayerPosition(px, py))
                .cameraPos(new CameraPosition(camX, camY))
                .build();
        Path file = Paths.get().getAutosave(save);
        Paths.get().deleteAutosave(save);
        GameStateIO.save(initial, file);

        try (GameServer server = new GameServer(GameServerConfig.builder()
                .saveName(save)
                .build());
             GameClient client = new GameClient()) {
            server.start();

            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

            com.badlogic.gdx.graphics.g2d.Batch batch =
                    org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.Batch.class);
            when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
            when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
            Stage stage = new Stage(new ScreenViewport(), batch);
            Settings settings = new Settings();
            try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
                var world = MapWorldBuilder.build(
                        MapWorldBuilder.builder(client.getMapState(), client, stage, new KeyBindings()),
                        null,
                        settings,
                        client.getMapState().cameraPos()
                );
                PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
                camera.toggleMode();
                world.process();

                IntBag players = world.getAspectSubscriptionManager()
                        .get(Aspect.all(PlayerComponent.class))
                        .getEntities();
                Entity player = world.getEntity(players.get(0));
                var pc = world.getMapper(PlayerComponent.class).get(player);
                final float epsilon = 0.01f;
                assertEquals(px * net.lapidist.colony.components.GameConstants.TILE_SIZE, pc.getX(), epsilon);
                assertEquals(py * net.lapidist.colony.components.GameConstants.TILE_SIZE, pc.getY(), epsilon);
                assertEquals(camX, camera.getCamera().position.x, epsilon);
                assertEquals(camY, camera.getCamera().position.y, epsilon);
                world.dispose();
            }
        }
    }
}
