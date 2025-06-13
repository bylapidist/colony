package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.components.state.PlayerPosition;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class GameServerPlayerPositionSaveTest {

    @Test
    public void playerPositionPersistsAcrossSaves() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("pos-save")
                .build();
        Paths.get().deleteAutosave("pos-save");
        GameServer server = new GameServer(config);
        server.start();

        Path saveFile = Paths.get().getAutosave("pos-save");
        final int x = 3;
        final int y = 4;
        PlayerPosition pos = new PlayerPosition(x, y);
        server.stop();
        GameStateIO.save(server.getMapState().toBuilder().playerPos(pos).build(), saveFile);

        GameServer server2 = new GameServer(config);
        server2.start();
        PlayerPosition loaded = server2.getMapState().playerPos();
        server2.stop();

        assertEquals(x, loaded.x());
        assertEquals(y, loaded.y());
    }
}
