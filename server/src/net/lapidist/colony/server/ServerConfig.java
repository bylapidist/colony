package net.lapidist.colony.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Configuration values for {@link GameServer}. The configuration is loaded
 * from {@code application.conf} on the classpath with default values applied
 * when properties are missing.
 */
public final class ServerConfig {

    public static final int DEFAULT_TCP_PORT = 54555;
    public static final int DEFAULT_UDP_PORT = 54777;
    public static final long DEFAULT_AUTOSAVE_INTERVAL_MS = 10 * 60 * 1000L;

    private final int tcpPort;
    private final int udpPort;
    private final long autosaveIntervalMs;

    public ServerConfig(
            final int tcpPortToSet,
            final int udpPortToSet,
            final long autosaveIntervalMsToSet
    ) {
        this.tcpPort = tcpPortToSet;
        this.udpPort = udpPortToSet;
        this.autosaveIntervalMs = autosaveIntervalMsToSet;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public long getAutosaveIntervalMs() {
        return autosaveIntervalMs;
    }

    /**
     * Loads configuration from {@code application.conf} on the classpath.
     * Missing properties fall back to {@link #DEFAULT_TCP_PORT},
     * {@link #DEFAULT_UDP_PORT} and {@link #DEFAULT_AUTOSAVE_INTERVAL_MS}.
     */
    public static ServerConfig load() {
        Config config = ConfigFactory.load().getConfig("server");
        int tcpPort = config.hasPath("tcpPort")
                ? config.getInt("tcpPort")
                : DEFAULT_TCP_PORT;
        int udpPort = config.hasPath("udpPort")
                ? config.getInt("udpPort")
                : DEFAULT_UDP_PORT;
        long interval = config.hasPath("autosaveIntervalMs")
                ? config.getLong("autosaveIntervalMs")
                : DEFAULT_AUTOSAVE_INTERVAL_MS;
        return new ServerConfig(tcpPort, udpPort, interval);
    }
}
