package net.lapidist.colony.components;

import com.artemis.Component;
import net.lapidist.colony.components.archetypes.TerrainType;

public class TerrainComponent extends Component {

    private TerrainType terrainType;

    public TerrainComponent() {
        this.terrainType = TerrainType.EMPTY;
    }

    public TerrainComponent(TerrainType terrainType) {
        this();

        setTerrainType(terrainType);
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
}
