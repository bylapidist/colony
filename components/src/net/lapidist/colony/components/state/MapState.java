package net.lapidist.colony.components.state;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MapState {
    public static final int CURRENT_VERSION = 1;

    private int version = CURRENT_VERSION;
    private String name = "map-" + UUID.randomUUID();
    private String description;

    private List<TileData> tiles = new ArrayList<>();
    private List<BuildingData> buildings = new ArrayList<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(final int versionToSet) {
        this.version = versionToSet;
    }

    public String getName() {
        return name;
    }

    public void setName(final String nameToSet) {
        this.name = nameToSet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }

    public List<TileData> getTiles() {
        return tiles;
    }

    public void setTiles(final List<TileData> tilesToSet) {
        this.tiles = tilesToSet;
    }

    public List<BuildingData> getBuildings() {
        return buildings;
    }

    public void setBuildings(final List<BuildingData> buildingsToSet) {
        this.buildings = buildingsToSet;
    }
}
