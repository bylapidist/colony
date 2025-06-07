package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.server.persistence.JavaGameStateSerializer;
import net.lapidist.colony.server.persistence.MapStateManager;
import net.lapidist.colony.server.persistence.SavePaths;

import java.io.IOException;

public final class GameServer {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    // Increase buffers so the entire map can be serialized in one object
    private static final int BUFFER_SIZE = 65536;
    private final Server server = new Server(BUFFER_SIZE, BUFFER_SIZE);
    private MapState mapState;

    public void start() throws IOException {
        registerClasses();
        server.start();
        server.bind(TCP_PORT, UDP_PORT);
        SavePaths.ensureDirectories();
        MapStateManager manager = new MapStateManager(
                new JavaGameStateSerializer<>(),
                SavePaths.getSaveFile()
        );
        mapState = manager.loadOrCreate();
        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                connection.sendTCP(mapState);
            }
        });
    }

    private void registerClasses() {
        server.getKryo().register(MapState.class);
        server.getKryo().register(TileData.class);
        server.getKryo().register(BuildingData.class);
        server.getKryo().register(java.util.ArrayList.class);
        server.getKryo().register(java.util.List.class);
    }

}
