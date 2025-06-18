package net.lapidist.colony.tests.mod;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.mod.ModInitTask;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.test.StubMod;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModInitTaskTest {

    private static final int WAIT_SECONDS = 5;

    private static final String META_FILE = "META-INF/services/net.lapidist.colony.mod.GameMod";

    private void createDirectoryMod(final Path dir, final String id) throws Exception {
        Files.createDirectories(dir.resolve("META-INF/services"));
        Files.writeString(dir.resolve("mod.json"), "{ id: \"" + id + "\", version: \"1\" }");
        Path service = dir.resolve(META_FILE);
        Files.writeString(service, StubMod.class.getName());
        Path classFile = Path.of(StubMod.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .resolve(StubMod.class.getName().replace('.', '/') + ".class");
        Path dest = dir.resolve(StubMod.class.getName().replace('.', '/') + ".class");
        Files.createDirectories(dest.getParent());
        Files.copy(classFile, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void loadsModsAsynchronously() throws Exception {
        System.clearProperty("stubmod.init");
        Path base = Files.createTempDirectory("mods-task");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);
        createDirectoryMod(mods.resolve("stub"), "stub");
        AtomicReference<Float> progress = new AtomicReference<>(0f);
        ModInitTask task = new ModInitTask(new ModLoader(paths), progress::set);
        CompletableFuture<List<LoadedMod>> future = CompletableFuture.supplyAsync(task::call);
        List<LoadedMod> modsLoaded = future.get(WAIT_SECONDS, TimeUnit.SECONDS);
        assertTrue(modsLoaded.stream().anyMatch(m -> "stub".equals(m.metadata().id())));
        assertEquals("true", System.getProperty("stubmod.init"));
        assertEquals(1f, progress.get(), 0f);
    }
}
