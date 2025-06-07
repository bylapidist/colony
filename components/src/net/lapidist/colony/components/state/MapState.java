package net.lapidist.colony.components.state;

import java.util.ArrayList;
import java.util.List;

import java.io.Serial;
import java.io.Serializable;

public final class MapState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
