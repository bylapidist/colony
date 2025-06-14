package net.lapidist.colony.config;

/**
 * Provides network related configuration values.
 */
public final class NetworkConfig {

    private NetworkConfig() {
    }

    /**
     * Returns the default server host.
     */
    public static String getHost() {
        return ColonyConfig.get().getString("game.server.host");
    }

    /**
     * Returns the TCP port number used by the server.
     */
    public static int getTcpPort() {
        return ColonyConfig.get().getInt("game.server.tcpPort");
    }

    /**
     * Returns the UDP port number used by the server.
     */
    public static int getUdpPort() {
        return ColonyConfig.get().getInt("game.server.udpPort");
    }
}
