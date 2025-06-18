package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.messages.TileSelectionData;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * Ensures message queues handle concurrent producers and consumers.
 */
public class GameClientQueueConcurrentTest {
    private static final int MESSAGE_COUNT = 1_000;

    @Test
    public void concurrentAccessDeliversAllMessages() throws Exception {
        // use default constructor without starting network
        GameClient client = new GameClient();
        int count = MESSAGE_COUNT;
        TileSelectionData update = new TileSelectionData(1, 1, true);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(1);
        AtomicInteger consumed = new AtomicInteger();

        Thread producer = new Thread(() -> {
            try {
                start.await();
            } catch (InterruptedException ignored) {
            }
            for (int i = 0; i < count; i++) {
                client.injectTileSelectionUpdate(update);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                start.await();
            } catch (InterruptedException ignored) {
            }
            TileSelectionData data;
            while (consumed.get() < count) {
                while ((data = client.poll(TileSelectionData.class)) != null) {
                    consumed.incrementAndGet();
                }
            }
            finished.countDown();
        });

        producer.start();
        consumer.start();
        start.countDown();
        producer.join();
        finished.await(2, TimeUnit.SECONDS);

        assertEquals(count, consumed.get());
    }
}
