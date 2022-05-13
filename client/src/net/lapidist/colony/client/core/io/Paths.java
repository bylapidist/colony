package net.lapidist.colony.client.core.io;

public final class Paths {

    static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";

    static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "/.colony";

    static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";

    static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "/saves";
}
