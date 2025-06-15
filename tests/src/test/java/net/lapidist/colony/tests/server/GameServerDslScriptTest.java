package net.lapidist.colony.tests.server;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.mod.test.StubMod;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;
import net.lapidist.colony.events.Events;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.assertEquals;

/** Test that a script using the DSL receives TileSelectionEvent. */
public class GameServerDslScriptTest {

    private static final String META_FILE = "META-INF/services/net.lapidist.colony.mod.GameMod";

    @Test
    public void dslScriptReceivesEvent() throws Exception {
        System.clearProperty("dsl.received");
        Path base = Files.createTempDirectory("dsl-script-mod-test");
        Paths paths = new Paths(new TestPathService(base));
        Path mods = paths.getModsFolder();
        Files.createDirectories(mods);
        Path dir = mods.resolve("dslmod");
        createScriptMod(dir);

        try (MockedStatic<Paths> mock = Mockito.mockStatic(Paths.class)) {
            mock.when(Paths::get).thenReturn(paths);
            GameServerConfig config = GameServerConfig.builder()
                    .saveName("dsl-script-mod")
                    .build();
            GameServer server = new GameServer(config);
            server.start();

            java.lang.reflect.Field f = GameServer.class.getDeclaredField("commandBus");
            f.setAccessible(true);
            CommandBus bus = (CommandBus) f.get(server);
            bus.dispatch(new TileSelectionCommand(0, 0, true));
            Events.update();

            server.stop();
        }
        assertEquals("true", System.getProperty("dsl.received"));
    }

    private void createScriptMod(final Path dir) throws IOException {
        Files.createDirectories(dir.resolve("META-INF/services"));
        Files.createDirectories(dir.resolve("scripts"));
        Files.writeString(dir.resolve("mod.json"), "{ id: \"dslmod\", version: \"1\", dependencies: [] }");
        Path service = dir.resolve(META_FILE);
        try (BufferedWriter w = Files.newBufferedWriter(service, StandardCharsets.UTF_8)) {
            w.write(StubMod.class.getName());
        }
        Path classFile = classFile();
        Path dest = dir.resolve(StubMod.class.getName().replace('.', '/') + ".class");
        Files.createDirectories(dest.getParent());
        Files.copy(classFile, dest, StandardCopyOption.REPLACE_EXISTING);
        Path script = dir.resolve("scripts/tileSelect.kts");
        Files.writeString(script, String.join(System.lineSeparator(),
                "import net.lapidist.colony.server.events.TileSelectionEvent",
                "",
                "on<TileSelectionEvent> { event ->",
                "    System.setProperty(\"dsl.received\", \"true\")",
                "}"
        ));
    }

    private Path classFile() throws IOException {
        return Path.of(StubMod.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .resolve(StubMod.class.getName().replace('.', '/') + ".class");
    }
}
