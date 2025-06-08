package net.lapidist.colony.io;

import java.util.Locale;

/**
 * Default {@link PathService} implementation using the host OS.
 */
public final class DefaultPathService extends AbstractPathService {

    private static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";
    private static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";

    private boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        return os.contains("windows");
    }

    @Override
    protected String getGameFolderPath() {
        return isWindows() ? GAME_FOLDER_WINDOWS : GAME_FOLDER_MAC;
    }
}
