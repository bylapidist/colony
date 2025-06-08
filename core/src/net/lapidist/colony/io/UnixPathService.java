package net.lapidist.colony.io;

/**
 * Unix like systems implementation of {@link PathService}.
 */
public final class UnixPathService extends AbstractPathService {

    private static final String GAME_FOLDER_UNIX = System.getProperty("user.home") + "/.colony";

    @Override
    protected String getGameFolderPath() {
        return GAME_FOLDER_UNIX;
    }
}
