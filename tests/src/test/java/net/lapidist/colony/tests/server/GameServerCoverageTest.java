package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.MapChunkRequest;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.services.AutosaveService;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.ResourceProductionService;
import net.lapidist.colony.server.services.MapService;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GameServerCoverageTest {

    private static Field field(final String name) throws Exception {
        Field f = GameServer.class.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    private static Method method(final String name, final Class<?>... types) throws Exception {
        Method m = GameServer.class.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m;
    }

    private static Method dispatch() throws Exception {
        Method m = GameServer.class.getSuperclass().getDeclaredMethod("dispatch", Object.class);
        m.setAccessible(true);
        return m;
    }

    @Test
    public void constructorWithHandlersStoresValues() throws Exception {
        GameServerConfig cfg = GameServerConfig.builder().saveName("ctor").build();
        Iterable<MessageHandler<?>> handlers = java.util.List.of(new AbstractMessageHandler<Object>(Object.class) {
            @Override
            public void handle(final Object message) { }
        });
        GameServer server = new GameServer(cfg, handlers);

        assertSame(handlers, field("handlers").get(server));
        assertNull(field("commandHandlers").get(server));
    }

    @Test
    public void broadcastDelegatesToNetworkService() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("bcast").build());
        NetworkService network = mock(NetworkService.class);
        field("networkService").set(server, network);

        Object message = new Object();
        server.broadcast(message);

        verify(network).broadcast(message);
    }

    @Test
    public void sendDelegatesToBroadcast() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("send").build());
        NetworkService network = mock(NetworkService.class);
        field("networkService").set(server, network);

        Object message = new Object();
        server.send(message);

        verify(network).broadcast(message);
    }

    @Test
    public void mapChunkRequestBroadcastsChunk() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("chunk").build());
        field("mapState").set(server, new MapState());
        NetworkService network = mock(NetworkService.class);
        field("networkService").set(server, network);

        server.registerDefaultHandlers();
        dispatch().invoke(server, new MapChunkRequest(0, 0));

        verify(network).broadcastChunk(any(MapState.class), eq(0), eq(0));
    }

    @Test
    public void stopDisposesMods() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("mods").build());
        field("networkService").set(server, mock(NetworkService.class));
        field("autosaveService").set(server, mock(AutosaveService.class));
        field("resourceProductionService").set(server, mock(ResourceProductionService.class));
        class DummyMod implements GameMod {
            private boolean disposed;

            @Override
            public void dispose() {
                disposed = true;
            }

            boolean isDisposed() {
                return disposed;
            }
        }
        DummyMod mod = new DummyMod();
        field("mods").set(server,
                java.util.List.of(new LoadedMod(mod, new ModMetadata("dummy", "1", java.util.List.of()))));

        server.stop();

        assertTrue(mod.isDisposed());
        verify((NetworkService) field("networkService").get(server)).stop();
        verify((AutosaveService) field("autosaveService").get(server)).stop();
        verify((ResourceProductionService) field("resourceProductionService").get(server)).stop();
    }

    @Test
    public void factoryAccessorsStoreValues() {
        GameServer server = new GameServer(GameServerConfig.builder().saveName("factories").build());
        java.util.function.Supplier<MapService> mapFactory = () -> null;
        java.util.function.Supplier<NetworkService> netFactory = () -> null;
        java.util.function.Supplier<AutosaveService> autoFactory = () -> null;
        java.util.function.Supplier<ResourceProductionService> prodFactory = () -> null;
        java.util.function.Supplier<CommandBus> busFactory = () -> null;

        server.setMapServiceFactory(mapFactory);
        server.setNetworkServiceFactory(netFactory);
        server.setAutosaveServiceFactory(autoFactory);
        server.setResourceProductionServiceFactory(prodFactory);
        server.setCommandBusFactory(busFactory);

        assertSame(mapFactory, server.getMapServiceFactory());
        assertSame(netFactory, server.getNetworkServiceFactory());
        assertSame(autoFactory, server.getAutosaveServiceFactory());
        assertSame(prodFactory, server.getResourceProductionServiceFactory());
        assertSame(busFactory, server.getCommandBusFactory());
    }
}
