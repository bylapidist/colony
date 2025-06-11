package net.lapidist.colony.io;

/**
 * Windows specific {@link PathService} implementation.
 */
public final class WindowsPathService extends AbstractPathService {

    @Override
    protected String getGameFolderPath() {
        String base = System.getenv("APPDATA");
        if (base == null || base.isBlank()) {
            base = System.getProperty("user.home");
        }
        return java.nio.file.Paths.get(base, ".colony").toString();
    }
}
