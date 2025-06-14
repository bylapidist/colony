package net.lapidist.colony.mod;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.lapidist.colony.io.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Discovers and loads {@link GameMod} implementations from the game\'s {@code mods/} directory.
 */
public final class ModLoader {

    /** Pair of a loaded {@link GameMod} and its associated metadata. */
    public record LoadedMod(GameMod mod, ModMetadata metadata) { }

    private static final Logger LOGGER = LoggerFactory.getLogger(ModLoader.class);

    private final Paths paths;

    public ModLoader(final Paths pathsToUse) {
        this.paths = pathsToUse;
    }

    /**
     * Scans the mods folder and instantiates all mods discovered.
     */
    public List<LoadedMod> loadMods() throws IOException {
        Path modsDir = paths.getModsFolder();
        if (!Files.exists(modsDir)) {
            return java.util.List.of();
        }

        List<LoadedMod> loaded = new ArrayList<>();
        Set<String> loadedIds = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            List<Path> entries = new ArrayList<>();
            for (Path entry : stream) {
                entries.add(entry);
            }
            entries.sort((a, b) -> a.getFileName().toString().compareToIgnoreCase(b.getFileName().toString()));
            for (Path entry : entries) {
                if (Files.isDirectory(entry)) {
                    loadFromDirectory(entry, loaded, loadedIds);
                } else if (entry.toString().endsWith(".jar")) {
                    loadFromJar(entry, loaded, loadedIds);
                }
            }
        }
        return loaded;
    }

    private void loadFromDirectory(final Path dir, final List<LoadedMod> out, final Set<String> loadedIds)
            throws IOException {
        Path meta = dir.resolve("mod.json");
        if (!Files.isRegularFile(meta)) {
            return;
        }
        ModMetadata metadata = readMetadata(Files.newBufferedReader(meta, StandardCharsets.UTF_8));
        if (!loadedIds.containsAll(metadata.dependencies())) {
            Set<String> missing = new HashSet<>(metadata.dependencies());
            missing.removeAll(loadedIds);
            LOGGER.warn("Skipping mod {} due to missing dependencies {}", metadata.id(), missing);
            return;
        }

        URLClassLoader cl = new URLClassLoader(new URL[]{dir.toUri().toURL()}, getClass().getClassLoader());
        int count = 0;
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(new LoadedMod(mod, metadata));
            count++;
        }
        if (count > 0) {
            loadedIds.add(metadata.id());
        }
    }

    private void loadFromJar(final Path jar, final List<LoadedMod> out, final Set<String> loadedIds)
            throws IOException {
        ModMetadata metadata;
        try (JarFile jf = new JarFile(jar.toFile())) {
            ZipEntry entry = jf.getEntry("mod.json");
            if (entry == null) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jf.getInputStream(entry), StandardCharsets.UTF_8))) {
                metadata = readMetadata(reader);
            }
        }
        if (!loadedIds.containsAll(metadata.dependencies())) {
            Set<String> missing = new HashSet<>(metadata.dependencies());
            missing.removeAll(loadedIds);
            LOGGER.warn("Skipping mod {} due to missing dependencies {}", metadata.id(), missing);
            return;
        }
        URLClassLoader cl = new URLClassLoader(new URL[]{jar.toUri().toURL()}, getClass().getClassLoader());
        int count = 0;
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(new LoadedMod(mod, metadata));
            count++;
        }
        if (count > 0) {
            loadedIds.add(metadata.id());
        }
    }

    private ModMetadata readMetadata(final BufferedReader reader) {
        Config config = ConfigFactory.parseReader(reader);
        List<String> deps = config.hasPath("dependencies") ? config.getStringList("dependencies") : List.of();
        return new ModMetadata(config.getString("id"), config.getString("version"), deps);
    }
}
