package net.lapidist.colony.config;

public final class NetworkConfig {

    private static final int TCP_PORT = ColonyConfig.get().getInt("game.server.tcpPort");
    private static final int UDP_PORT = ColonyConfig.get().getInt("game.server.udpPort");
    private static final String HOST = ColonyConfig.get().hasPath("game.server.host")
            ? ColonyConfig.get().getString("game.server.host")
            : "localhost";

    private NetworkConfig() {
    }

    public static int getTcpPort() {
        return TCP_PORT;
    }

    public static int getUdpPort() {
        return UDP_PORT;
    }

    public static String getHost() {
        return HOST;
    }
}
