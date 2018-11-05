package net.lapidist.colony.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.Constants;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle(Constants.NAME + " " + Constants.VERSION);
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);

        new Lwjgl3Application(new Colony(), config);
    }
}
