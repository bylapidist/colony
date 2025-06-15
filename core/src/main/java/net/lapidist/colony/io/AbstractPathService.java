package net.lapidist.colony.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base {@link PathService} implementation providing shared logic.
 */
abstract class AbstractPathService implements PathService {

    private static final String AUTOSAVE_SUFFIX = Paths.AUTOSAVE_SUFFIX;
    private static final String SETTINGS_FILE = "settings.properties";
    private static final String CONFIG_FILE = "config.conf";

    protected abstract String getGameFolderPath();

    private FileHandle getGameFolder() {
        if (Gdx.files != null) {
            return Gdx.files.external(".colony");
        }
        return new FileHandle(getGameFolderPath());
    }

    private FileHandle getSaveFolder() {
        return getGameFolder().child("saves");
    }

    private FileHandle getSettingsHandle() {
        return getGameFolder().child(SETTINGS_FILE);
    }

    private FileHandle getConfigHandle() {
        return getGameFolder().child(CONFIG_FILE);
    }

    private void ensureExists(final FileHandle handle) {
        if (!handle.exists()) {
            handle.mkdirs();
        }
    }

    @Override
    public void createGameFoldersIfNotExists() throws IOException {
        FileHandle gameFolder = getGameFolder();
        ensureExists(gameFolder);

        FileHandle saveFolder = getSaveFolder();
        ensureExists(saveFolder);
        FileHandle modsFolder = getGameFolder().child("mods");
        ensureExists(modsFolder);
    }

    @Override
    public Path getSettingsFile() throws IOException {
        createGameFoldersIfNotExists();
        return getSettingsHandle().file().toPath();
    }

    @Override
    public Path getSaveFile(final String fileName) throws IOException {
        createGameFoldersIfNotExists();
        return getSaveFolder().child(fileName).file().toPath();
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
        FileHandle folder = getSaveFolder();
        if (!folder.exists()) {
            return Collections.emptyList();
        }
        int suffixLength = AUTOSAVE_SUFFIX.length();
        List<String> saves = new ArrayList<>();
        for (FileHandle file : folder.list()) {
            String name = file.name();
            if (name.endsWith(AUTOSAVE_SUFFIX)) {
                saves.add(name.substring(0, name.length() - suffixLength));
            }
        }
        return saves;
    }

    @Override
    public void deleteAutosave(final String saveName) throws IOException {
        FileHandle file = getSaveFolder().child(saveName + AUTOSAVE_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public Path getConfigFile() throws IOException {
        createGameFoldersIfNotExists();
        return getConfigHandle().file().toPath();
    }

    @Override
    public Path getModsFolder() throws IOException {
        createGameFoldersIfNotExists();
        FileHandle folder = getGameFolder().child("mods");
        ensureExists(folder);
        return folder.file().toPath();
    }
}
