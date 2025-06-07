package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@KryoType
public final class MapState {
    public static final int CURRENT_VERSION = 2;

    private int version = CURRENT_VERSION;
    private String name = "map-" + UUID.randomUUID();
    private String saveName = "save-" + UUID.randomUUID();
    private String autosaveName;
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

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(final String saveNameToSet) {
        this.saveName = saveNameToSet;
    }

    public String getAutosaveName() {
        return autosaveName;
    }

    public void setAutosaveName(final String autosaveNameToSet) {
        this.autosaveName = autosaveNameToSet;
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
