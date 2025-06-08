package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@KryoType
public record MapState(
        int version,
        String name,
        String saveName,
        String autosaveName,
        String description,
        List<TileData> tiles,
        List<BuildingData> buildings
) {
    public static final int CURRENT_VERSION = 2;

    public MapState() {
        this(
                CURRENT_VERSION,
                "map-" + UUID.randomUUID(),
                "save-" + UUID.randomUUID(),
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public MapState withVersion(final int version) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withName(final String name) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withSaveName(final String saveName) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withAutosaveName(final String autosaveName) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withDescription(final String description) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withTiles(final List<TileData> tiles) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withBuildings(final List<BuildingData> buildings) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, buildings);
    }
}
