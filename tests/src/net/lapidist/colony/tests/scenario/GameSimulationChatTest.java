package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ChatMessageData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class GameSimulationChatTest {
    private static final int WAIT_MS = 100;

    @Test
    public void chatLogDisplaysMessages() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient sender = new GameClient();
        sender.start();
        GameClient receiver = new GameClient();
        receiver.start();

        MapState state = receiver.getMapState();
        GameSimulation sim = new GameSimulation(state, receiver);

        sender.sendChatMessage(new ChatMessageData("A", "hello"));
        Thread.sleep(WAIT_MS);
        sim.step();

        ChatMessageData data = receiver.pollChatMessage();
        assertEquals("hello", data.message());

        sender.stop();
        receiver.stop();
        server.stop();
    }
}
