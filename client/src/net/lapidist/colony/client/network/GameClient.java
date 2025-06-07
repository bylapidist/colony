package net.lapidist.colony.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.server.GameServer;

import java.io.IOException;

public final class GameClient {
    private final Client client = new Client();
    private MapState mapState;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int WAIT_TIME_MS = 10;

    public void start() throws IOException, InterruptedException {
        registerClasses();
        client.start();
        client.addListener(new Listener() {
            @Override
            public void received(final Connection connection, final Object object) {
                if (object instanceof MapState) {
                    mapState = (MapState) object;
                }
            }
        });
        client.connect(CONNECT_TIMEOUT, "localhost", GameServer.TCP_PORT, GameServer.UDP_PORT);
        while (mapState == null) {
            Thread.sleep(WAIT_TIME_MS);
        }
    }

    private void registerClasses() {
        client.getKryo().register(MapState.class);
        client.getKryo().register(TileData.class);
        client.getKryo().register(BuildingData.class);
        client.getKryo().register(java.util.ArrayList.class);
        client.getKryo().register(java.util.List.class);
    }

    public MapState getMapState() {
        return mapState;
    }
}
