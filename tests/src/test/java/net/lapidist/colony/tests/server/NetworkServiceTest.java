package net.lapidist.colony.tests.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.components.state.MapChunkBytes;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.server.services.NetworkService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class NetworkServiceTest {

    @Test
    public void startBindsServerAndSendsStateOnConnect() throws Exception {
        Server server = mock(Server.class);
        com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();
        net.lapidist.colony.serialization.KryoRegistry.register(kryo);
        when(server.getKryo()).thenReturn(kryo);
        NetworkService service = new NetworkService(server, 1, 2);
        MapState state = new MapState();
        state.putTile(new TileData());

        service.start(state, o -> { });

        verify(server).start();
        verify(server).bind(1, 2);
        ArgumentCaptor<Listener> captor = ArgumentCaptor.forClass(Listener.class);
        verify(server).addListener(captor.capture());

        Listener listener = captor.getValue();
        Connection connection = mock(Connection.class);
        listener.connected(connection);
        ArgumentCaptor<MapMetadata> metaCaptor = ArgumentCaptor.forClass(MapMetadata.class);
        verify(connection).sendTCP(metaCaptor.capture());
        MapMetadata meta = metaCaptor.getValue();
        assertEquals(net.lapidist.colony.components.GameConstants.MAP_WIDTH, meta.width());
        assertEquals(net.lapidist.colony.components.GameConstants.MAP_HEIGHT, meta.height());
        verify(connection, atLeastOnce()).sendTCP(isA(MapChunkBytes.class));
    }

    @Test
    public void broadcastUsesServerSend() {
        Server server = mock(Server.class);
        NetworkService service = new NetworkService(server, 1, 2);
        Object message = new Object();

        service.broadcast(message);

        verify(server).sendToAllTCP(message);
    }

    @Test
    public void broadcastChunkSendsData() {
        Server server = mock(Server.class);
        com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();
        net.lapidist.colony.serialization.KryoRegistry.register(kryo);
        when(server.getKryo()).thenReturn(kryo);
        NetworkService service = new NetworkService(server, 1, 2);
        MapState state = new MapState();
        state.putTile(new TileData());

        service.broadcastChunk(state, 0, 0);

        verify(server).sendToAllTCP(isA(MapChunkBytes.class));
    }

    @Test
    public void stopStopsServer() {
        Server server = mock(Server.class);
        NetworkService service = new NetworkService(server, 1, 2);

        service.stop();

        verify(server).stop();
    }
}
