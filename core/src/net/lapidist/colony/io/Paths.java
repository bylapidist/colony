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

    private static final Paths INSTANCE = new Paths(createDefaultService());

    private final PathService service;

    public static Paths get() {
        return INSTANCE;
    }

    public Paths(final PathService serviceToUse) {
        this.service = serviceToUse;
    }

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

    public void createGameFoldersIfNotExists() throws IOException {
        service.createGameFoldersIfNotExists();
    }

    public Path getSettingsFile() throws IOException {
        return service.getSettingsFile();
    }

    public Path getSaveFile(final String fileName) throws IOException {
        return service.getSaveFile(fileName);
    }

    public Path getSave(final String saveName) throws IOException {
        return service.getSave(saveName);
    }

    public Path getAutosave(final String saveName) throws IOException {
        return service.getAutosave(saveName);
    }

    public Path getLastAutosaveMarker() throws IOException {
        return service.getLastAutosaveMarker();
    }

    public List<String> listAutosaves() throws IOException {
        return service.listAutosaves();
    }

    public void deleteAutosave(final String saveName) throws IOException {
        service.deleteAutosave(saveName);
    }
}
