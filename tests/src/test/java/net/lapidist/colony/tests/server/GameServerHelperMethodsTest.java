package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.io.Paths;
import com.esotericsoftware.kryonet.Server;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GameServerHelperMethodsTest {

    private static Method method(final Class<?> c, final String name, final Class<?>... types) throws Exception {
        Method m = c.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m;
    }

    private static <T> T getField(final Object o, final String name) throws Exception {
        Field f = o.getClass().getDeclaredField(name);
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        T value = (T) f.get(o);
        return value;
    }

    @Test
    public void initKryoRegistersClasses() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("init").build());
        method(GameServer.class, "initKryo").invoke(server);
        Server kryoServer = getField(server, "server");
        assertNotNull(kryoServer.getKryo().getRegistration(MapState.class));
    }

    @Test
    public void loadMapStateCreatesSave() throws Exception {
        String name = "load-state";
        Paths.get().deleteAutosave(name);
        GameServer server = new GameServer(GameServerConfig.builder().saveName(name).build());
        method(GameServer.class, "loadMapState").invoke(server);
        MapState state = getField(server, "mapState");
        assertNotNull(state);
        assertTrue(Files.exists(Paths.get().getAutosave(name)));
    }


    @Test
    public void registerDefaultHandlersDispatchesMessages() throws Exception {
        String name = "register";
        Paths.get().deleteAutosave(name);
        GameServer server = new GameServer(GameServerConfig.builder().saveName(name).build());
        method(GameServer.class, "loadMapState").invoke(server);
        method(GameServer.class, "registerDefaultHandlers").invoke(server);
        Method dispatch = method(GameServer.class.getSuperclass(), "dispatch", Object.class);
        dispatch.invoke(server, new TileSelectionData(0, 0, true));
        MapState state = server.getMapState();
        assertTrue(state.tiles().get(new TilePos(0, 0)).selected());
    }
}
