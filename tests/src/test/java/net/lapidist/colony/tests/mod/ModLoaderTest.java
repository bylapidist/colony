package net.lapidist.colony.tests.mod;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.test.StubMod;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModLoaderTest {

    private static final String META_FILE = "META-INF/services/net.lapidist.colony.mod.GameMod";

    @Test
    public void loadsJarMod() throws Exception {
        System.clearProperty("stubmod.init");
        Path base = Files.createTempDirectory("mods-test");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path jar = mods.resolve("stub.jar");
        createJarMod(jar, "stub", List.of());

        ModLoader loader = new ModLoader(paths);
        List<LoadedMod> modsLoaded = loader.loadMods();
        for (LoadedMod mod : modsLoaded) {
            mod.mod().init();
        }

        assertTrue(modsLoaded.stream().anyMatch(m -> m.mod().getClass().getName().equals(StubMod.class.getName())));
        assertTrue(modsLoaded.stream().anyMatch(m -> m.metadata().id().equals("stub")));
        assertEquals("true", System.getProperty("stubmod.init"));
    }

    @Test
    public void loadsDirectoryMod() throws Exception {
        System.clearProperty("stubmod.init");
        Path base = Files.createTempDirectory("mods-test-dir");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path dir = mods.resolve("stub");
        createDirectoryMod(dir, "stub", List.of());

        ModLoader loader = new ModLoader(paths);
        List<LoadedMod> modsLoaded = loader.loadMods();
        for (LoadedMod mod : modsLoaded) {
            mod.mod().init();
        }

        assertTrue(modsLoaded.stream().anyMatch(m -> m.mod().getClass().getName().equals(StubMod.class.getName())));
        assertTrue(modsLoaded.stream().anyMatch(m -> m.metadata().id().equals("stub")));
        assertEquals("true", System.getProperty("stubmod.init"));
    }

    @Test
    public void skipsModWithMissingDependency() throws Exception {
        Path base = Files.createTempDirectory("mods-test-missing");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path jar = mods.resolve("dep.jar");
        createJarMod(jar, "dep", List.of("absent"));

        Logger logger = (Logger) LoggerFactory.getLogger(ModLoader.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        ModLoader loader = new ModLoader(paths);
        List<LoadedMod> modsLoaded = loader.loadMods();

        logger.detachAppender(appender);

        assertTrue(modsLoaded.stream().noneMatch(m -> m.metadata().id().equals("dep")));
    }

    @Test
    public void loadsModWhenDependencyMet() throws Exception {
        Path base = Files.createTempDirectory("mods-test-dep-ok");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path baseJar = mods.resolve("00-base.jar");
        createJarMod(baseJar, "base", List.of());
        Path childJar = mods.resolve("01-child.jar");
        createJarMod(childJar, "child", List.of("base"));

        ModLoader loader = new ModLoader(paths);
        List<LoadedMod> modsLoaded = loader.loadMods();

        assertTrue(modsLoaded.size() >= 2);
        assertTrue(modsLoaded.stream().anyMatch(m -> m.metadata().id().equals("child")));
    }

    private void createJarMod(final Path jar, final String id, final List<String> deps) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            // mod.json
            jos.putNextEntry(new JarEntry("mod.json"));
            jos.write(json(id, deps).getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();

            // service descriptor
            jos.putNextEntry(new JarEntry(META_FILE));
            jos.write((StubMod.class.getName() + "\n").getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();

            // class file
            Path classFile = classFile();
            jos.putNextEntry(new JarEntry(StubMod.class.getName().replace('.', '/') + ".class"));
            jos.write(Files.readAllBytes(classFile));
            jos.closeEntry();
        }
    }

    private void createDirectoryMod(final Path dir, final String id, final List<String> deps) throws IOException {
        Files.createDirectories(dir.resolve("META-INF/services"));
        Files.writeString(dir.resolve("mod.json"), json(id, deps));
        Path service = dir.resolve(META_FILE);
        try (BufferedWriter w = Files.newBufferedWriter(service, StandardCharsets.UTF_8)) {
            w.write(StubMod.class.getName());
        }
        Path classFile = classFile();
        Path dest = dir.resolve(StubMod.class.getName().replace('.', '/') + ".class");
        Files.createDirectories(dest.getParent());
        Files.copy(classFile, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    private Path classFile() throws IOException {
        return Path.of(StubMod.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .resolve(StubMod.class.getName().replace('.', '/') + ".class");
    }

    private String json(final String id, final List<String> deps) {
        String joined = String.join("\", \"", deps);
        return "{ id: \"" + id + "\", version: \"1\", dependencies: ["
                + (joined.isEmpty() ? "" : "\"" + joined + "\"") + "] }";
    }
}
