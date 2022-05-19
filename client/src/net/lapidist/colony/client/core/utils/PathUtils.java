package net.lapidist.colony.client.core.utils;

import net.lapidist.colony.client.core.io.FileLocation;

import java.io.File;
import java.io.IOException;

public final class PathUtils {

    private PathUtils() {
    }

    static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";

    static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";

    static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";

    static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";

    public static String getGameFolder() {
        if (OSUtils.isMac()) {
            return GAME_FOLDER_MAC;
        }
        if (OSUtils.isWindows()) {
            return GAME_FOLDER_WINDOWS;
        }
        throw new RuntimeException("Cannot get game folder - unknown operating system");
    }

    public static String getSaveFolder() {
        if (OSUtils.isMac()) {
            return SAVE_FOLDER_MAC;
        }
        if (OSUtils.isWindows()) {
            return SAVE_FOLDER_WINDOWS;
        }
        throw new RuntimeException("Cannot get save folder - unknown operating system");
    }

    public static void createGameFoldersIfNotExists() throws IOException {
        if (!FileLocation.EXTERNAL.getFile(getGameFolder()).isDirectory()) {
            if (new File(getGameFolder()).mkdirs()) {
                System.out.printf(
                        "[%s] Created game folder: \"%s\"\n",
                        PathUtils.class.getSimpleName(),
                        getGameFolder()
                );
            }
        }

        if (!FileLocation.EXTERNAL.getFile(getSaveFolder()).isDirectory()) {
            if (new File(getSaveFolder()).mkdirs()) {
                System.out.printf(
                        "[%s] Created saves folder: \"%s\"\n",
                        PathUtils.class.getSimpleName(),
                        getSaveFolder()
                );
            }
        }
    }
}
