package net.lapidist.colony.client.core.io;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Paths {

    private static final Logger LOGGER = LoggerFactory.getLogger(Paths.class);

    private Paths() { }

    static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";

    static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";

    static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";

    static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";

    public static void createGameFoldersIfNotExists() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String gameFolderPath = "";
        String saveFolderPath = "";

        if (os.contains("windows")) {
            gameFolderPath = GAME_FOLDER_WINDOWS;
            saveFolderPath = SAVE_FOLDER_WINDOWS;
        } else if (os.contains("mac") || os.contains("darwin")) {
            gameFolderPath = GAME_FOLDER_MAC;
            saveFolderPath = SAVE_FOLDER_MAC;
        } else {
            throw new IOException("Unknown OS when trying to create game directories, reported OS is: " + os);
        }

        if (!FileLocation.EXTERNAL.getFile(gameFolderPath).isDirectory()) {
            if (new File(gameFolderPath).mkdirs()) {
                LOGGER.info("Created game folder: \"{}\"", gameFolderPath);
            }
        }

        if (!FileLocation.EXTERNAL.getFile(saveFolderPath).isDirectory()) {
            if (new File(saveFolderPath).mkdirs()) {
                LOGGER.info("Created saves folder: \"{}\"", saveFolderPath);
            }
        }
    }
}
