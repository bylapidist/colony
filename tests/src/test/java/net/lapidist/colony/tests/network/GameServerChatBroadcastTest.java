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
        GameServerConfig config = GameServerConfig.builder()
                .saveName("chat-broadcast")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("chat-broadcast");
        GameServer server = new GameServer(config);
        server.start();

        try (GameClient clientA = new GameClient();
             GameClient clientB = new GameClient()) {
            CountDownLatch latchA = new CountDownLatch(1);
            clientA.start(state -> latchA.countDown());
            CountDownLatch latchB = new CountDownLatch(1);
            clientB.start(state -> latchB.countDown());
            latchA.await(1, TimeUnit.SECONDS);
            latchB.await(1, TimeUnit.SECONDS);

            ChatMessage msg = new ChatMessage("hello");
            clientA.sendChatMessage(msg);
            Thread.sleep(WAIT_MS);

            ChatMessage received = clientB.poll(ChatMessage.class);
            assertNotNull(received);
            assertEquals(msg.text(), received.text());
        }
        server.stop();
    }
}
