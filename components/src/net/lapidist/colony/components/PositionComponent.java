package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {

    private Vector2 position;

    public final Vector2 getPosition() {
        return position;
    }

    public final void setPosition(final Vector2 positionToSet) {
        this.position = positionToSet;
    }
}
