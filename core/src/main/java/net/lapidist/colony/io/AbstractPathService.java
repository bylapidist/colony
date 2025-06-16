package net.lapidist.colony.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Base {@link PathService} implementation providing shared logic.
 */
abstract class AbstractPathService implements PathService {

    private static final String AUTOSAVE_SUFFIX = Paths.AUTOSAVE_SUFFIX;
    private static final String SETTINGS_FILE = "settings.conf";

    protected abstract String getGameFolderPath();

    private Path getGameFolder() {
        return java.nio.file.Paths.get(getGameFolderPath());
    }

    private Path getSaveFolder() {
        return getGameFolder().resolve("saves");
    }

    private Path getSettingsHandle() {
        return getGameFolder().resolve(SETTINGS_FILE);
    }

    private void ensureExists(final Path path) throws IOException {
        Files.createDirectories(path);
    }

    @Override
    public void createGameFoldersIfNotExists() throws IOException {
        Path gameFolder = getGameFolder();
        ensureExists(gameFolder);

        Path saveFolder = getSaveFolder();
        ensureExists(saveFolder);
        Path modsFolder = getGameFolder().resolve("mods");
        ensureExists(modsFolder);
    }

    @Override
    public Path getSettingsFile() throws IOException {
        createGameFoldersIfNotExists();
        return getSettingsHandle();
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
        if (Files.notExists(folder)) {
            return Collections.emptyList();
        }
        int suffixLength = AUTOSAVE_SUFFIX.length();
        List<String> saves = new ArrayList<>();
        try (Stream<Path> stream = Files.list(folder)) {
            stream.forEach(p -> {
                String name = p.getFileName().toString();
                if (name.endsWith(AUTOSAVE_SUFFIX)) {
                    saves.add(name.substring(0, name.length() - suffixLength));
                }
            });
        }
        return saves;
    }

    @Override
    public void deleteAutosave(final String saveName) throws IOException {
        Path file = getSaveFolder().resolve(saveName + AUTOSAVE_SUFFIX);
        Files.deleteIfExists(file);
    }

    @Override
    public Path getModsFolder() throws IOException {
        createGameFoldersIfNotExists();
        Path folder = getGameFolder().resolve("mods");
        ensureExists(folder);
        return folder;
    }
}
