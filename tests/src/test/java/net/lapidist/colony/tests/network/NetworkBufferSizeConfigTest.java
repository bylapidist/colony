package net.lapidist.colony.tests.network;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.config.ColonyConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkBufferSizeConfigTest {

    @Test
    public void readsBufferSizeFromConfig() {
        int expected = ColonyConfig.get().getInt("game.networkBufferSize");
        assertEquals(expected, GameConstants.NETWORK_BUFFER_SIZE);
    }
}
