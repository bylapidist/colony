package net.lapidist.colony.io;

/**
 * Unix like systems implementation of {@link PathService}.
 */
public final class UnixPathService extends AbstractPathService {

    @Override
    protected String getGameFolderPath() {
        String home = System.getProperty("user.home");
        return java.nio.file.Paths.get(home, ".colony").toString();
    }
}
