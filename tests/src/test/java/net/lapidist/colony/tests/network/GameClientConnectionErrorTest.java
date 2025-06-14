package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;

public class GameClientConnectionErrorTest {

    @Test
    public void callbackInvokedWhenConnectionFails() throws Exception {
        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.setConnectionErrorCallback(e -> latch.countDown());
        client.start();
        latch.await(1, TimeUnit.SECONDS);
        assertNull(client.getMapState());
        client.stop();
    }
}
