package net.lapidist.colony.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

// OS specific path services

/**
 * Shared utility methods for resolving save locations.
 * Uses Java NIO so that both the client and server can access
 * the same directories without relying on LibGDX specific APIs.
 */
public final class Paths {

    public static final String AUTOSAVE_SUFFIX = "-autosave.dat";

    private static PathService service = createDefaultService();

    private Paths() { }

    /**
     * Creates the default {@link PathService} for the current operating system.
     */
    public static PathService createDefaultService() {
        String os = System.getProperty("os.name").toLowerCase(java.util.Locale.ENGLISH);
        if (os.contains("windows")) {
            return new WindowsPathService();
        }
        return new UnixPathService();
    }

    /**
     * Replace the underlying {@link PathService} implementation.
     * Primarily used for testing.
     *
     * @param newService service to use
     */
    public static void setService(final PathService newService) {
        service = newService;
    }

    public static void createGameFoldersIfNotExists() throws IOException {
        service.createGameFoldersIfNotExists();
    }

    public static Path getSettingsFile() throws IOException {
        return service.getSettingsFile();
    }

    public static Path getSaveFile(final String fileName) throws IOException {
        return service.getSaveFile(fileName);
    }

    public static Path getSave(final String saveName) throws IOException {
        return service.getSave(saveName);
    }

    public static Path getAutosave(final String saveName) throws IOException {
        return service.getAutosave(saveName);
    }

    public static Path getLastAutosaveMarker() throws IOException {
        return service.getLastAutosaveMarker();
    }

    public static List<String> listAutosaves() throws IOException {
        return service.listAutosaves();
    }

    public static void deleteAutosave(final String saveName) throws IOException {
        service.deleteAutosave(saveName);
    }
}
