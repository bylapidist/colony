package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.components.state.CameraPosition;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class GameServerCameraPositionSaveTest {

    @Test
    public void cameraPositionPersistsAcrossSaves() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("camera-save")
                .build();
        Paths.get().deleteAutosave("camera-save");
        GameServer server = new GameServer(config);
        server.start();

        Path saveFile = Paths.get().getAutosave("camera-save");
        final float x = 5f;
        final float y = 6f;
        final float epsilon = 0.001f;
        CameraPosition pos = new CameraPosition(x, y);
        server.stop();
        GameStateIO.save(server.getMapState().toBuilder().cameraPos(pos).build(), saveFile);

        GameServer server2 = new GameServer(config);
        server2.start();
        CameraPosition loaded = server2.getMapState().cameraPos();
        server2.stop();

        assertEquals(x, loaded.x(), epsilon);
        assertEquals(y, loaded.y(), epsilon);
    }
}
