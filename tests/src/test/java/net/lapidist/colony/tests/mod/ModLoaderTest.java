package net.lapidist.colony.tests.mod;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.test.StubMod;
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
        Path base = Files.createTempDirectory("mods-test");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path jar = mods.resolve("stub.jar");
        createJarMod(jar);

        ModLoader loader = new ModLoader(paths);
        List<GameMod> modsLoaded = loader.loadMods();
        for (GameMod mod : modsLoaded) {
            mod.init();
        }

        assertTrue(modsLoaded.stream().anyMatch(m -> m.getClass().getName().equals(StubMod.class.getName())));
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
        createDirectoryMod(dir);

        ModLoader loader = new ModLoader(paths);
        List<GameMod> modsLoaded = loader.loadMods();
        for (GameMod mod : modsLoaded) {
            mod.init();
        }

        assertTrue(modsLoaded.stream().anyMatch(m -> m.getClass().getName().equals(StubMod.class.getName())));
        assertEquals("true", System.getProperty("stubmod.init"));
    }

    private void createJarMod(final Path jar) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            // mod.json
            jos.putNextEntry(new JarEntry("mod.json"));
            jos.write(json().getBytes(StandardCharsets.UTF_8));
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

    private void createDirectoryMod(final Path dir) throws IOException {
        Files.createDirectories(dir.resolve("META-INF/services"));
        Files.writeString(dir.resolve("mod.json"), json());
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

    private String json() {
        return "{ id: \"stub\", version: \"1\", dependencies: [] }";
    }
}
