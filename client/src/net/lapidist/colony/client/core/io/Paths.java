package net.lapidist.colony.client.core.io;

import net.lapidist.colony.client.core.utils.OS;

import java.io.File;
import java.io.IOException;

public final class Paths {

    private Paths() {
    }

    static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";

    static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";

    static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";

    static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";

    public static String getGameFolder() {
        if (OS.isMac()) {
            return GAME_FOLDER_MAC;
        }
        if (OS.isWindows()) {
            return GAME_FOLDER_WINDOWS;
        }
        throw new RuntimeException("Cannot get game folder - unknown operating system");
    }

    public static String getSaveFolder() {
        if (OS.isMac()) {
            return SAVE_FOLDER_MAC;
        }
        if (OS.isWindows()) {
            return SAVE_FOLDER_WINDOWS;
        }
        throw new RuntimeException("Cannot get save folder - unknown operating system");
    }

    public static void createGameFoldersIfNotExists() throws IOException {
        if (!FileLocation.EXTERNAL.getFile(getGameFolder()).isDirectory()) {
            if (new File(getGameFolder()).mkdirs()) {
                System.out.printf(
                        "[%s] Created game folder: \"%s\"\n",
                        Paths.class.getSimpleName(),
                        getGameFolder()
                );
            }
        }

        if (!FileLocation.EXTERNAL.getFile(getSaveFolder()).isDirectory()) {
            if (new File(getSaveFolder()).mkdirs()) {
                System.out.printf(
                        "[%s] Created saves folder: \"%s\"\n",
                        Paths.class.getSimpleName(),
                        getSaveFolder()
                );
            }
        }
    }
}
