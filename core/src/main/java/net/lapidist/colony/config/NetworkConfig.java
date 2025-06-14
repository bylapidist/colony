package net.lapidist.colony.config;

public final class NetworkConfig {

    private NetworkConfig() {
    }

    public static int getTcpPort() {
        return ColonyConfig.get().getInt("game.server.tcpPort");
    }

    public static int getUdpPort() {
        return ColonyConfig.get().getInt("game.server.udpPort");
    }

    public static String getHost() {
        return ColonyConfig.get().getString("game.server.host");
    }
}
