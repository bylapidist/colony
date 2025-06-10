package net.lapidist.colony.io;

/**
 * Unix like systems implementation of {@link PathService}.
 */
public final class UnixPathService extends AbstractPathService {

    @Override
    protected java.nio.file.Path getGameFolderPath() {
        String home = System.getProperty("user.home");
        return java.nio.file.Paths.get(home, ".colony");
    }
}
