package net.lapidist.colony.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base {@link PathService} implementation providing shared logic.
 */
abstract class AbstractPathService implements PathService {

    private static final String AUTOSAVE_SUFFIX = Paths.AUTOSAVE_SUFFIX;
    private static final String SETTINGS_FILE = "settings.properties";

    protected abstract Path getGameFolderPath();

    private Path getGameFolder() {
        return GdxPathAdapter.resolveGameFolder(getGameFolderPath());
    }

    private Path getSaveFolder() {
        return getGameFolder().resolve("saves");
    }

    private Path getSettingsPath() {
        return getGameFolder().resolve(SETTINGS_FILE);
    }

    private void ensureExists(final Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public void createGameFoldersIfNotExists() throws IOException {
        Path gameFolder = getGameFolder();
        ensureExists(gameFolder);

        Path saveFolder = getSaveFolder();
        ensureExists(saveFolder);
    }

    @Override
    public Path getSettingsFile() throws IOException {
        createGameFoldersIfNotExists();
        return getSettingsPath();
    }

    @Override
    public Path getSaveFile(final String fileName) throws IOException {
        createGameFoldersIfNotExists();
        return getSaveFolder().resolve(fileName);
    }

    @Override
    public Path getSave(final String saveName) throws IOException {
        return getSaveFile(saveName + ".dat");
    }

    @Override
    public Path getAutosave(final String saveName) throws IOException {
        return getSaveFile(saveName + AUTOSAVE_SUFFIX);
    }

    @Override
    public Path getLastAutosaveMarker() throws IOException {
        return getSaveFile("lastautosave.txt");
    }

    @Override
    public List<String> listAutosaves() throws IOException {
        Path folder = getSaveFolder();
        if (!Files.exists(folder)) {
            return Collections.emptyList();
        }
        int suffixLength = AUTOSAVE_SUFFIX.length();
        try (var stream = Files.list(folder)) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(AUTOSAVE_SUFFIX))
                    .map(name -> name.substring(0, name.length() - suffixLength))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteAutosave(final String saveName) throws IOException {
        Path file = getSaveFolder().resolve(saveName + AUTOSAVE_SUFFIX);
        Files.deleteIfExists(file);
    }
}
