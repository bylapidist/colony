package net.lapidist.colony.save;

import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Migration from save version 23 to 24 converting enum names to registry IDs. */
public final class V23ToV24Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V23.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V24.number();
    }

    @Override
    public MapState apply(final MapState state) {
        for (MapChunkData chunk : state.chunks().values()) {
            Map<TilePos, TileData> tiles = chunk.getTiles();
            for (Map.Entry<TilePos, TileData> entry : tiles.entrySet()) {
                TileData td = entry.getValue();
                var def = Registries.tiles().get(td.tileType());
                if (def != null) {
                    String id = def.id();
                    if (!id.equals(td.tileType())) {
                        tiles.put(entry.getKey(), td.toBuilder().tileType(id).build());
                    }
                }
            }
        }

        List<BuildingData> buildings = new ArrayList<>(state.buildings().size());
        for (BuildingData bd : state.buildings()) {
            var def = Registries.buildings().get(bd.buildingType());
            String id = def != null ? def.id() : bd.buildingType();
            buildings.add(new BuildingData(bd.x(), bd.y(), id));
        }

        return state.toBuilder()
                .buildings(buildings)
                .version(toVersion())
                .build();
    }
}
