package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.ServerConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerConfigTest {

    @Test
    public void loadDefaultConfig() {
        ServerConfig config = ServerConfig.load();
        assertEquals(ServerConfig.DEFAULT_TCP_PORT, config.getTcpPort());
        assertEquals(ServerConfig.DEFAULT_UDP_PORT, config.getUdpPort());
        assertEquals(ServerConfig.DEFAULT_AUTOSAVE_INTERVAL_MS, config.getAutosaveIntervalMs());
    }
}
