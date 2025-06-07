package net.lapidist.colony.server.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Utility methods for resolving save locations. Java NIO is used instead of
 * LibGDX because the server runs without an initialized Gdx environment.
 */

public final class Paths {

    public static final String AUTOSAVE_SUFFIX = "-autosave.dat";

    private Paths() { }

    static final String GAME_FOLDER_MAC = System.getProperty("user.home") + "/.colony";
    static final String GAME_FOLDER_WINDOWS = System.getenv("APPDATA") + "\\.colony";
    static final String SAVE_FOLDER_MAC = GAME_FOLDER_MAC + "/saves";
    static final String SAVE_FOLDER_WINDOWS = GAME_FOLDER_WINDOWS + "\\saves";

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

        // Using java.nio.Paths for cross-platform path resolution
        java.nio.file.Path gameFolder = java.nio.file.Paths.get(gameFolderPath);
        if (!Files.isDirectory(gameFolder)) {
            Files.createDirectories(gameFolder);
        }

        java.nio.file.Path saveFolder = java.nio.file.Paths.get(saveFolderPath);
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
        // Path resolves directly to the OS specific location
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

    public static java.util.List<String> listAutosaves() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String saveFolder;
        if (os.contains("windows")) {
            saveFolder = SAVE_FOLDER_WINDOWS;
        } else {
            saveFolder = SAVE_FOLDER_MAC;
        }
        java.nio.file.Path folder = java.nio.file.Paths.get(saveFolder);
        if (!Files.isDirectory(folder)) {
            return java.util.Collections.emptyList();
        }
        try (java.util.stream.Stream<Path> stream = Files.list(folder)) {
            int suffixLength = AUTOSAVE_SUFFIX.length();
            return stream
                    .filter(p -> p.getFileName().toString().endsWith(AUTOSAVE_SUFFIX))
                    .map(p -> p.getFileName().toString())
                    .map(name -> name.substring(0, name.length() - suffixLength))
                    .toList();
        }
    }
}
