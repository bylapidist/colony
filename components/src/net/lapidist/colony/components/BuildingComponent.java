package net.lapidist.colony.components;

import com.artemis.Component;
import net.lapidist.colony.components.archetypes.BuildingType;

public class BuildingComponent extends Component {

    private BuildingType buildingType;

    public BuildingComponent() {
        this.buildingType = BuildingType.EMPTY;
    }

    public BuildingComponent(BuildingType buildingType) {
        this();

        setBuildingType(buildingType);
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
