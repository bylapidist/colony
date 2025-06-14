package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/** Unit tests for {@link GameClient#processChunkRequestQueue(int)}. */
public class GameClientChunkRequestQueueTest {

    @Test
    public void processesLimitedNumberOfChunkRequests() {
        GameClient client = Mockito.spy(new GameClient(java.util.List.of()));
        Mockito.doNothing().when(client).send(any());

        int total = GameClient.CHUNK_REQUEST_BATCH_SIZE * 2;
        for (int i = 0; i < total; i++) {
            client.queueChunkRequest(i, 0);
        }

        client.processChunkRequestQueue(GameClient.CHUNK_REQUEST_BATCH_SIZE);
        verify(client, times(GameClient.CHUNK_REQUEST_BATCH_SIZE)).send(any());

        client.processChunkRequestQueue(GameClient.CHUNK_REQUEST_BATCH_SIZE);
        verify(client, times(total)).send(any());
    }
}
