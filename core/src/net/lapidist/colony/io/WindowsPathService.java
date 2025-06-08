package net.lapidist.colony.io;

/**
 * Windows specific {@link PathService} implementation.
 */
public final class WindowsPathService extends AbstractPathService {

    private static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";

    @Override
    protected String getGameFolderPath() {
        return GAME_FOLDER_WINDOWS;
    }
}
