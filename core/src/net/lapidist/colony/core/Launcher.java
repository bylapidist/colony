package net.lapidist.colony.core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Launcher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle(Constants.NAME + " " + Constants.VERSION);
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);

        new Lwjgl3Application(new Colony(), config);
    }
}
