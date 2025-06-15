package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;
import net.lapidist.colony.io.Paths;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Tests that built-in mods register services and handlers automatically. */
public class GameServerBaseModsTest {

    private static <T> T field(final Object obj, final String name) throws Exception {
        Field f = GameServer.class.getDeclaredField(name);
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        T value = (T) f.get(obj);
        return value;
    }

    @Test
    public void servicesAndHandlersRegistered() throws Exception {
        String name = "base-mods";
        Paths.get().deleteAutosave(name);
        GameServer server = new GameServer(GameServerConfig.builder().saveName(name).build());
        server.start();

        assertNotNull(field(server, "mapService"));
        assertNotNull(field(server, "networkService"));
        assertNotNull(field(server, "autosaveService"));
        assertNotNull(field(server, "resourceProductionService"));
        CommandBus bus = field(server, "commandBus");
        assertNotNull(bus);

        Method dispatch = AbstractMessageEndpoint.class.getDeclaredMethod("dispatch", Object.class);
        dispatch.setAccessible(true);
        dispatch.invoke(server, new TileSelectionData(0, 0, true));
        assertTrue(server.getMapState().getTile(0, 0).selected());

        bus.dispatch(new TileSelectionCommand(1, 1, true));
        assertTrue(server.getMapState().getTile(1, 1).selected());

        server.stop();
    }
}
