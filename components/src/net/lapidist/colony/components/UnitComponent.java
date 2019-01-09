package net.lapidist.colony.components;

import com.artemis.Component;
import net.lapidist.colony.components.archetypes.UnitType;

public class UnitComponent extends Component {

    private UnitType unitType;

    public UnitComponent() {
        this.unitType = UnitType.EMPTY;
    }

    public UnitComponent(UnitType unitType) {
        this();

        setUnitType(unitType);
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
}
