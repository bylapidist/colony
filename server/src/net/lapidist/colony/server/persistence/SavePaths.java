package net.lapidist.colony.server.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public final class SavePaths {
    private static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";
    private static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";
    private static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";
    private static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";
    private static final String SAVE_FILE_NAME = "map.dat";

    private SavePaths() { }

    public static File getSaveFile() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String saveFolderPath;
        if (os.contains("windows")) {
            saveFolderPath = SAVE_FOLDER_WINDOWS;
        } else if (os.contains("mac") || os.contains("darwin")) {
            saveFolderPath = SAVE_FOLDER_MAC;
        } else {
            throw new IOException(
                    "Unknown OS when trying to locate save directories, reported OS is: " + os);
        }
        return new File(saveFolderPath, SAVE_FILE_NAME);
    }

    public static void ensureDirectories() throws IOException {
        File saveFile = getSaveFile();
        File dir = saveFile.getParentFile();
        if (!dir.isDirectory() && !dir.mkdirs()) {
            throw new IOException("Unable to create save directory: " + dir.getAbsolutePath());
        }
    }
}
