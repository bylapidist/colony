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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

    private record ScannedMod(Path path, boolean jar, ModMetadata metadata) {
    }

    /**
     * Scans the mods folder and instantiates all mods discovered.
     */
    public List<LoadedMod> loadMods() throws IOException {
        Path modsDir = paths.getModsFolder();
        if (!Files.exists(modsDir)) {
            return java.util.List.of();
        }

        List<ScannedMod> scanned = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            List<Path> entries = new ArrayList<>();
            for (Path entry : stream) {
                entries.add(entry);
            }
            entries.sort((a, b) -> a.getFileName().toString().compareToIgnoreCase(b.getFileName().toString()));
            for (Path entry : entries) {
                ScannedMod mod = null;
                if (Files.isDirectory(entry)) {
                    mod = scanDirectory(entry);
                } else if (entry.toString().endsWith(".jar")) {
                    mod = scanJar(entry);
                }
                if (mod != null) {
                    scanned.add(mod);
                }
            }
        }

        List<ScannedMod> ordered = resolveOrder(scanned);

        List<LoadedMod> loaded = new ArrayList<>();
        for (ScannedMod mod : ordered) {
            if (mod.jar()) {
                loadFromJar(mod.path(), mod.metadata(), loaded);
            } else {
                loadFromDirectory(mod.path(), mod.metadata(), loaded);
            }
        }
        return loaded;
    }

    private void loadFromDirectory(final Path dir, final ModMetadata metadata, final List<LoadedMod> out)
            throws IOException {
        URLClassLoader cl = new URLClassLoader(new URL[]{dir.toUri().toURL()}, getClass().getClassLoader());
        executeDirectoryScripts(dir, cl);
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(new LoadedMod(mod, metadata));
        }
    }

    private ScannedMod scanDirectory(final Path dir) throws IOException {
        Path meta = dir.resolve("mod.json");
        if (!Files.isRegularFile(meta)) {
            return null;
        }
        ModMetadata metadata = readMetadata(Files.newBufferedReader(meta, StandardCharsets.UTF_8));
        return new ScannedMod(dir, false, metadata);
    }

    private ScannedMod scanJar(final Path jar) throws IOException {
        ModMetadata metadata;
        try (JarFile jf = new JarFile(jar.toFile())) {
            ZipEntry entry = jf.getEntry("mod.json");
            if (entry == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jf.getInputStream(entry), StandardCharsets.UTF_8))) {
                metadata = readMetadata(reader);
            }
        }
        return new ScannedMod(jar, true, metadata);
    }

    private List<ScannedMod> resolveOrder(final List<ScannedMod> scanned) {
        Map<String, ScannedMod> byId = new HashMap<>();
        for (ScannedMod mod : scanned) {
            byId.putIfAbsent(mod.metadata().id(), mod);
        }

        Map<String, List<String>> deps = new HashMap<>();
        for (ScannedMod mod : scanned) {
            deps.put(mod.metadata().id(), mod.metadata().dependencies());
        }

        Map<String, List<String>> missing = new HashMap<>();
        Set<String> invalid = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (ScannedMod mod : scanned) {
                String id = mod.metadata().id();
                if (invalid.contains(id)) {
                    continue;
                }
                for (String dep : deps.get(id)) {
                    if (!byId.containsKey(dep) || invalid.contains(dep)) {
                        invalid.add(id);
                        missing.computeIfAbsent(id, k -> new ArrayList<>()).add(dep);
                        changed = true;
                        break;
                    }
                }
            }
        }

        for (String id : invalid) {
            LOGGER.warn("Skipping mod {} due to missing dependencies {}", id, missing.get(id));
        }

        Map<String, ScannedMod> valid = new HashMap<>();
        for (ScannedMod mod : scanned) {
            if (!invalid.contains(mod.metadata().id())) {
                valid.put(mod.metadata().id(), mod);
            }
        }

        Map<String, Set<String>> dependents = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        for (ScannedMod mod : valid.values()) {
            String id = mod.metadata().id();
            List<String> d = deps.get(id);
            indegree.put(id, d.size());
            for (String dep : d) {
                dependents.computeIfAbsent(dep, k -> new HashSet<>()).add(id);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) {
                queue.add(e.getKey());
            }
        }

        List<ScannedMod> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            String id = queue.poll();
            ordered.add(valid.get(id));
            for (String dep : dependents.getOrDefault(id, Set.of())) {
                int deg = indegree.computeIfPresent(dep, (k, v) -> v - 1);
                if (deg == 0) {
                    queue.add(dep);
                }
            }
            indegree.remove(id);
        }

        if (!indegree.isEmpty()) {
            for (String id : indegree.keySet()) {
                LOGGER.warn("Skipping mod {} due to dependency cycle", id);
            }
        }

        return ordered;
    }

    private void loadFromJar(final Path jar, final ModMetadata metadata, final List<LoadedMod> out)
            throws IOException {
        URLClassLoader cl = new URLClassLoader(new URL[]{jar.toUri().toURL()}, getClass().getClassLoader());
        executeJarScripts(jar, cl);
        for (GameMod mod : ServiceLoader.load(GameMod.class, cl)) {
            out.add(new LoadedMod(mod, metadata));
        }
    }

    private ModMetadata readMetadata(final BufferedReader reader) {
        Config config = ConfigFactory.parseReader(reader);
        List<String> deps = config.hasPath("dependencies") ? config.getStringList("dependencies") : List.of();
        return new ModMetadata(config.getString("id"), config.getString("version"), deps);
    }

    private void executeDirectoryScripts(final Path dir, final ClassLoader cl) throws IOException {
        Path scripts = dir.resolve("scripts");
        if (!Files.isDirectory(scripts)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(scripts, "*.kts")) {
            for (Path script : stream) {
                try (BufferedReader reader = Files.newBufferedReader(script, StandardCharsets.UTF_8)) {
                    executeScript(reader, cl, script.toString());
                }
            }
        }
    }

    private void executeJarScripts(final Path jar, final ClassLoader cl) throws IOException {
        try (JarFile jf = new JarFile(jar.toFile())) {
            var entries = jf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (!e.isDirectory() && e.getName().startsWith("scripts/") && e.getName().endsWith(".kts")) {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(jf.getInputStream(e), StandardCharsets.UTF_8))) {
                        executeScript(reader, cl, e.getName());
                    }
                }
            }
        }
    }

    private void executeScript(final BufferedReader reader, final ClassLoader cl,
                               final String name) throws IOException {
        ScriptEngine engine = new ScriptEngineManager(cl).getEngineByExtension("kts");
        if (engine == null) {
            // Fallback to the default classloader which contains the scripting engine
            engine = new ScriptEngineManager().getEngineByExtension("kts");
        }
        if (engine == null) {
            LOGGER.warn("Kotlin scripting engine not available, skipping {}", name);
            return;
        }
        try {
            engine.eval(reader);
            LOGGER.debug("Executed script {}", name);
        } catch (ScriptException e) {
            LOGGER.error("Failed to execute script {}", name, e);
        }
    }
}
