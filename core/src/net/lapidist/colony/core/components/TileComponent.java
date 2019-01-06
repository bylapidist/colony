package net.lapidist.colony.core.components;

import com.badlogic.ashley.core.Component;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.core.entities.BuildingType;
import net.lapidist.colony.core.entities.TerrainType;
import net.lapidist.colony.core.entities.UnitType;

public class TileComponent implements Component {

    public ITile tile;
    public TerrainType terrainType;
    public UnitType unitType;
    public BuildingType buildingType;
    public boolean active;
    public boolean hovered;
}
