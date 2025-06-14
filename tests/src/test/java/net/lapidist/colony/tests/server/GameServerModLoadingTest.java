package net.lapidist.colony.tests.server;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.test.StubMod;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.Assert.assertTrue;

/** Verifies the server loads mods and starts correctly. */
public class GameServerModLoadingTest {

    private static final String META_FILE = "META-INF/services/net.lapidist.colony.mod.GameMod";

    @Test
    public void loadsModsAndStarts() throws Exception {
        System.clearProperty("stubmod.init");
        Path base = Files.createTempDirectory("server-mod-test");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);

        Path jar = mods.resolve("stub.jar");
        createJarMod(jar);

        try (MockedStatic<Paths> mock = Mockito.mockStatic(Paths.class)) {
            mock.when(Paths::get).thenReturn(paths);

            GameServerConfig config = GameServerConfig.builder()
                    .saveName("mod-test")
                    .build();
            GameServer server = new GameServer(config);
            server.start();

            java.lang.reflect.Field f = GameServer.class.getDeclaredField("mods");
            f.setAccessible(true);
            List<LoadedMod> loaded = (List<LoadedMod>) f.get(server);
            assertTrue(loaded.stream().anyMatch(m -> "base-map-service".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "base-network".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "base-autosave".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "base-resource-production".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "base-handlers".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "base-definitions".equals(m.metadata().id())));
            assertTrue(loaded.stream().anyMatch(m -> "stub".equals(m.metadata().id())));
            assertTrue(server.isRunning());
            server.stop();
        }
        assertTrue("true".equals(System.getProperty("stubmod.init")));
    }

    private void createJarMod(final Path jar) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            jos.putNextEntry(new JarEntry("mod.json"));
            jos.write("{ id: \"stub\", version: \"1\", dependencies: [] }".getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();

            jos.putNextEntry(new JarEntry(META_FILE));
            jos.write((StubMod.class.getName() + "\n").getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();

            Path classFile = classFile();
            jos.putNextEntry(new JarEntry(StubMod.class.getName().replace('.', '/') + ".class"));
            jos.write(Files.readAllBytes(classFile));
            jos.closeEntry();
        }
    }

    private Path classFile() throws IOException {
        return Path.of(StubMod.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .resolve(StubMod.class.getName().replace('.', '/') + ".class");
    }
}
