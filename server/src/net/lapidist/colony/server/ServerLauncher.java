package net.lapidist.colony.server;

import java.io.IOException;

public final class ServerLauncher {
    private ServerLauncher() { }

    public static void main(final String[] args) throws IOException {
        GameServer server = new GameServer();
        server.start();
    }
}
