package net.lapidist.colony.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

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

    private Paths() {
    }

    public static void createGameFoldersIfNotExists() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String gameFolderPath;
        String saveFolderPath;

        if (os.contains("windows")) {
            gameFolderPath = GAME_FOLDER_WINDOWS;
            saveFolderPath = SAVE_FOLDER_WINDOWS;
        } else {
            gameFolderPath = GAME_FOLDER_MAC;
            saveFolderPath = SAVE_FOLDER_MAC;
        }

        Path gameFolder = java.nio.file.Paths.get(gameFolderPath);
        if (!Files.isDirectory(gameFolder)) {
            Files.createDirectories(gameFolder);
        }

        Path saveFolder = java.nio.file.Paths.get(saveFolderPath);
        if (!Files.isDirectory(saveFolder)) {
            Files.createDirectories(saveFolder);
        }
    }

    public static Path getSaveFile(final String fileName) throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String saveFolder;
        if (os.contains("windows")) {
            saveFolder = SAVE_FOLDER_WINDOWS;
        } else {
            saveFolder = SAVE_FOLDER_MAC;
        }
        return java.nio.file.Paths.get(saveFolder, fileName);
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
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String saveFolder;
        if (os.contains("windows")) {
            saveFolder = SAVE_FOLDER_WINDOWS;
        } else {
            saveFolder = SAVE_FOLDER_MAC;
        }
        Path folder = java.nio.file.Paths.get(saveFolder);
        if (!Files.isDirectory(folder)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.list(folder)) {
            int suffixLength = AUTOSAVE_SUFFIX.length();
            return stream
                    .filter(p -> p.getFileName().toString().endsWith(AUTOSAVE_SUFFIX))
                    .map(p -> p.getFileName().toString())
                    .map(name -> name.substring(0, name.length() - suffixLength))
                    .toList();
        }
    }
}
