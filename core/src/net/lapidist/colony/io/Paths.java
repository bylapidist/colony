package net.lapidist.colony.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Shared utility methods for resolving save locations.
 * Uses Java NIO so that both the client and server can access
 * the same directories without relying on LibGDX specific APIs.
 */
public final class Paths {

    public static final String AUTOSAVE_SUFFIX = "-autosave.dat";

    private static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";
    private static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";
    private static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";
    private static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";
    private static final String SETTINGS_FILE = "settings.properties";

    private Paths() {
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        return os.contains("windows");
    }

    private static String getGameFolderPath() {
        return isWindows() ? GAME_FOLDER_WINDOWS : GAME_FOLDER_MAC;
    }

    private static String getSaveFolderPath() {
        return isWindows() ? SAVE_FOLDER_WINDOWS : SAVE_FOLDER_MAC;
    }

    private static FileHandle getGameFolder() {
        if (Gdx.files != null) {
            return Gdx.files.external(".colony");
        }
        return new FileHandle(getGameFolderPath());
    }

    private static FileHandle getSaveFolder() {
        return getGameFolder().child("saves");
    }

    private static FileHandle getSettingsHandle() {
        return getGameFolder().child(SETTINGS_FILE);
    }

    public static void createGameFoldersIfNotExists() throws IOException {
        FileHandle gameFolder = getGameFolder();
        if (!gameFolder.exists()) {
            gameFolder.mkdirs();
        }

        FileHandle saveFolder = getSaveFolder();
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
    }

    public static Path getSettingsFile() throws IOException {
        createGameFoldersIfNotExists();
        return getSettingsHandle().file().toPath();
    }

    public static Path getSaveFile(final String fileName) throws IOException {
        createGameFoldersIfNotExists();
        return getSaveFolder().child(fileName).file().toPath();
    }

    public static Path getSave(final String saveName) throws IOException {
        return getSaveFile(saveName + ".dat");
    }

    public static Path getAutosave(final String saveName) throws IOException {
        return getSaveFile(saveName + AUTOSAVE_SUFFIX);
    }

    public static Path getLastAutosaveMarker() throws IOException {
        return getSaveFile("lastautosave.txt");
    }

    public static List<String> listAutosaves() throws IOException {
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

    public static void deleteAutosave(final String saveName) throws IOException {
        FileHandle file = getSaveFolder().child(saveName + AUTOSAVE_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
    }
}
