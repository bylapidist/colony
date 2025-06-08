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

    public MapState withVersion(final int newVersion) {
        return new MapState(newVersion, name, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withName(final String newName) {
        return new MapState(version, newName, saveName, autosaveName, description, tiles, buildings);
    }

    public MapState withSaveName(final String newSaveName) {
        return new MapState(version, name, newSaveName, autosaveName, description, tiles, buildings);
    }

    public MapState withAutosaveName(final String newAutosaveName) {
        return new MapState(version, name, saveName, newAutosaveName, description, tiles, buildings);
    }

    public MapState withDescription(final String newDescription) {
        return new MapState(version, name, saveName, autosaveName, newDescription, tiles, buildings);
    }

    public MapState withTiles(final List<TileData> newTiles) {
        return new MapState(version, name, saveName, autosaveName, description, newTiles, buildings);
    }

    public MapState withBuildings(final List<BuildingData> newBuildings) {
        return new MapState(version, name, saveName, autosaveName, description, tiles, newBuildings);
    }
}
