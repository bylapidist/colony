package net.lapidist.colony.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.server.GameServer;

import java.io.IOException;

public final class ClientLauncher {

    private ClientLauncher() {
    }

    public static void main(final String[] args) {
        Thread serverThread = new Thread(() -> {
            try {
                new GameServer().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setTitle(Constants.NAME + " " + Constants.VERSION);
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
        config.setIdleFPS(Constants.TARGET_FPS);
        config.setHdpiMode(HdpiMode.Logical);

        new Lwjgl3Application(new Colony(), config);
    }
}
