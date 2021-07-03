package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {

    private Vector3 position;

    public final Vector3 getPosition() {
        return position;
    }

    public final void setPosition(final Vector3 positionToSet) {
        this.position = positionToSet;
    }
}
