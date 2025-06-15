package net.lapidist.colony.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Resolves save and settings file locations.
 */
public interface PathService {
    void createGameFoldersIfNotExists() throws IOException;

    Path getSettingsFile() throws IOException;

    Path getSaveFile(String fileName) throws IOException;

    Path getSave(String saveName) throws IOException;

    Path getAutosave(String saveName) throws IOException;

    Path getLastAutosaveMarker() throws IOException;

    List<String> listAutosaves() throws IOException;

    void deleteAutosave(String saveName) throws IOException;

    Path getConfigFile() throws IOException;

    Path getModsFolder() throws IOException;
}
