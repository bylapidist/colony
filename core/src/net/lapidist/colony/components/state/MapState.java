package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;
import net.lapidist.colony.save.SaveVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@KryoType
public record MapState(
        int version,
        String name,
        String saveName,
        String autosaveName,
        String description,
        Map<TilePos, TileData> tiles,
        List<BuildingData> buildings,
        ResourceData playerResources
) {
    public static final int CURRENT_VERSION = SaveVersion.CURRENT.number();

    public MapState() {
        this(
                CURRENT_VERSION,
                "map-" + UUID.randomUUID(),
                "save-" + UUID.randomUUID(),
                null,
                null,
                new HashMap<>(),
                new ArrayList<>(),
                new ResourceData()
        );
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int version;
        private String name;
        private String saveName;
        private String autosaveName;
        private String description;
        private Map<TilePos, TileData> tiles;
        private List<BuildingData> buildings;
        private ResourceData playerResources;

        private Builder() {
            this(new MapState());
        }

        private Builder(final MapState state) {
            this.version = state.version;
            this.name = state.name;
            this.saveName = state.saveName;
            this.autosaveName = state.autosaveName;
            this.description = state.description;
            this.tiles = state.tiles;
            this.buildings = state.buildings;
            this.playerResources = state.playerResources;
        }

        public Builder version(final int newVersion) {
            this.version = newVersion;
            return this;
        }

        public Builder name(final String newName) {
            this.name = newName;
            return this;
        }

        public Builder saveName(final String newSaveName) {
            this.saveName = newSaveName;
            return this;
        }

        public Builder autosaveName(final String newAutosaveName) {
            this.autosaveName = newAutosaveName;
            return this;
        }

        public Builder description(final String newDescription) {
            this.description = newDescription;
            return this;
        }

        public Builder tiles(final Map<TilePos, TileData> newTiles) {
            this.tiles = newTiles;
            return this;
        }

        public Builder buildings(final List<BuildingData> newBuildings) {
            this.buildings = newBuildings;
            return this;
        }

        public Builder playerResources(final ResourceData newResources) {
            this.playerResources = newResources;
            return this;
        }

        public MapState build() {
            return new MapState(version, name, saveName, autosaveName, description, tiles, buildings, playerResources);
        }
    }
}
