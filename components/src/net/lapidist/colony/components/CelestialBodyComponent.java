package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;

public abstract class CelestialBodyComponent implements Component {

    private CelestialBodyComponent parent;

    public final CelestialBodyComponent getParent() {
        return parent;
    }

    public final void setParent(final CelestialBodyComponent parentToSet) {
        this.parent = parentToSet;
    }
}
