package net.lapidist.colony.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.lapidist.colony.Colony;
import net.lapidist.colony.Constants;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Constants.NAME + " " + Constants.VERSION;
        config.width = Constants.WIDTH;
        config.height = Constants.HEIGHT;
//        config.resizable = false;

        new LwjglApplication(new Colony(), config);
    }
}
