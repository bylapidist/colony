package net.lapidist.colony.mod;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.lapidist.colony.io.Paths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Discovers and loads {@link GameMod} implementations from the game\'s {@code mods/} directory.
 */
public final class ModLoader {

    private final Paths paths;

    public ModLoader(final Paths pathsToUse) {
        this.paths = pathsToUse;
    }

    /**
     * Scans the mods folder and instantiates all mods discovered.
     */
    public List<GameMod> loadMods() throws IOException {
        Path modsDir = paths.getModsFolder();
        if (!Files.exists(modsDir)) {
            return java.util.List.of();
        }

        List<GameMod> loaded = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    loadFromDirectory(entry, loaded);
                } else if (entry.toString().endsWith(".jar")) {
                    loadFromJar(entry, loaded);
                }
            }
        }
        return loaded;
    }

    private void loadFromDirectory(final Path dir, final List<GameMod> out) throws IOException {
        Path meta = dir.resolve("mod.json");
        if (!Files.isRegularFile(meta)) {
            return;
        }
        readMetadata(Files.newBufferedReader(meta, StandardCharsets.UTF_8));
        URLClassLoader cl = new URLClassLoader(new URL[]{dir.toUri().toURL()}, getClass().getClassLoader());
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(mod);
        }
    }

    private void loadFromJar(final Path jar, final List<GameMod> out) throws IOException {
        try (JarFile jf = new JarFile(jar.toFile())) {
            ZipEntry entry = jf.getEntry("mod.json");
            if (entry == null) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jf.getInputStream(entry), StandardCharsets.UTF_8))) {
                readMetadata(reader);
            }
        }
        URLClassLoader cl = new URLClassLoader(new URL[]{jar.toUri().toURL()}, getClass().getClassLoader());
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(mod);
        }
    }

    private ModMetadata readMetadata(final BufferedReader reader) {
        Config config = ConfigFactory.parseReader(reader);
        List<String> deps = config.hasPath("dependencies") ? config.getStringList("dependencies") : List.of();
        return new ModMetadata(config.getString("id"), config.getString("version"), deps);
    }
}
