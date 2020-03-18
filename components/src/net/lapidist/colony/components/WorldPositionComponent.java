package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class WorldPositionComponent extends Component {

    private Vector3 position;

    public final Vector3 getPosition() {
        return position;
    }

    public final void setPosition(final Vector3 positionToSet) {
        this.position = positionToSet;
    }
}
