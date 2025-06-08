package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.ChatMessageData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerChatTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverBroadcastsChatMessage() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient sender = new GameClient();
        sender.start();
        GameClient receiver = new GameClient();
        receiver.start();

        ChatMessageData message = new ChatMessageData("A", "hello");
        sender.sendChatMessage(message);

        Thread.sleep(WAIT_MS);

        ChatMessageData result = receiver.pollChatMessage();
        assertNotNull(result);
        assertEquals(message.player(), result.player());
        assertEquals(message.message(), result.message());

        sender.stop();
        receiver.stop();
        server.stop();
    }
}
