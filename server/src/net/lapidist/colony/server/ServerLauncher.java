package net.lapidist.colony.server;

import java.io.IOException;


public final class ServerLauncher {
    private ServerLauncher() { }

    public static void main(final String[] args) throws IOException {
        try (GameServer server = new GameServer(GameServerConfig.builder().build())) {
            server.start();
        }
    }
}
