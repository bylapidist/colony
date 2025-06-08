package net.lapidist.colony.tests.network;

import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerChatBroadcastTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverBroadcastsChatMessages() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient clientA = new GameClient();
        CountDownLatch latchA = new CountDownLatch(1);
        clientA.start(state -> latchA.countDown());
        GameClient clientB = new GameClient();
        CountDownLatch latchB = new CountDownLatch(1);
        clientB.start(state -> latchB.countDown());
        latchA.await(1, TimeUnit.SECONDS);
        latchB.await(1, TimeUnit.SECONDS);

        ChatMessage msg = new ChatMessage("hello");
        clientA.sendChatMessage(msg);
        Thread.sleep(WAIT_MS);

        ChatMessage received = clientB.pollChatMessage();
        assertNotNull(received);
        assertEquals(msg.text(), received.text());

        clientA.stop();
        clientB.stop();
        server.stop();
    }
}
