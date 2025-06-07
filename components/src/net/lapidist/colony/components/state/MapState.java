package net.lapidist.colony.components.state;

import java.util.ArrayList;
import java.util.List;

public final class MapState {
    private List<TileData> tiles = new ArrayList<>();
    private List<BuildingData> buildings = new ArrayList<>();

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
