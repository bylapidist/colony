package net.lapidist.colony.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.settings.GraphicsSettings;

public final class ClientLauncher {

    private static final int COLOR_BITS = 8;
    private static final int DEPTH_BITS = 16;
    private static final int STENCIL_BITS = 8;
    private static final int MSAA_SAMPLES = 4;

    private ClientLauncher() {
    }

    public static void main(final String[] args) {
        Lwjgl3ApplicationConfiguration config = createConfiguration();

        new Lwjgl3Application(new Colony(), config);
    }

    public static Lwjgl3ApplicationConfiguration createConfiguration() {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setTitle(Constants.NAME + " " + Constants.VERSION);
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
        config.setIdleFPS(Constants.TARGET_FPS);
        config.setHdpiMode(HdpiMode.Logical);

        GraphicsSettings graphics;
        try {
            graphics = net.lapidist.colony.settings.Settings.load().getGraphicsSettings();
        } catch (Exception e) {
            graphics = new GraphicsSettings();
        }
        if (graphics.isAntialiasingEnabled()) {
            config.setBackBufferConfig(
                    COLOR_BITS,
                    COLOR_BITS,
                    COLOR_BITS,
                    COLOR_BITS,
                    DEPTH_BITS,
                    STENCIL_BITS,
                    MSAA_SAMPLES
            );
        }

        return config;
    }
}
