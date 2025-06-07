package net.lapidist.colony.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

import java.io.IOException;
import java.util.Random;

public final class GameServer {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    private final Server server = new Server();
    private MapState mapState;

    public void start() throws IOException {
        registerClasses();
        server.start();
        server.bind(TCP_PORT, UDP_PORT);
        generateMap();
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

    private void generateMap() {
        mapState = new MapState();
        Random random = new Random();
        String[] textures = new String[]{"grass0", "dirt0"};
        for (int x = 0; x <= GameConstants.MAP_WIDTH; x++) {
            for (int y = 0; y <= GameConstants.MAP_HEIGHT; y++) {
                TileData tile = new TileData();
                tile.setX(x);
                tile.setY(y);
                tile.setTileType("GRASS");
                tile.setTextureRef(textures[random.nextInt(textures.length)]);
                tile.setPassable(true);
                mapState.getTiles().add(tile);
            }
        }
        BuildingData building = new BuildingData();
        building.setX(1);
        building.setY(1);
        building.setBuildingType("HOUSE");
        building.setTextureRef("house0");
        mapState.getBuildings().add(building);
    }
}
