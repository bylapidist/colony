package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.test.MapTickerMod;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

/** Scenario ensuring a mod can register a custom system. */
@RunWith(GdxTestRunner.class)
public class GameSimulationCustomSystemTest {

    private static final int AUTOSAVE_INTERVAL = 20;
    private static final int SLEEP_MS = 60;

    @Test
    public void modUpdatesMapStateEachTick() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-custom-system")
                .autosaveInterval(AUTOSAVE_INTERVAL)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-custom-system");
        MapTickerMod.TICKS.set(0);
        try (MockedConstruction<ModLoader> loader = mockConstruction(ModLoader.class,
                (m, c) -> when(m.loadMods()).thenReturn(List.of(
                        new LoadedMod(new MapTickerMod(), new ModMetadata("tick", "1", List.of())))));
             GameServer server = new GameServer(config)) {
            server.start();
            Thread.sleep(SLEEP_MS);

            assertTrue(MapTickerMod.TICKS.get() > 0);
            assertTrue(server.getMapState().description().startsWith("tick"));
        }
    }
}
