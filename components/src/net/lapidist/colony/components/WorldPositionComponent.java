package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class WorldPositionComponent extends Component {

    private Vector3 position;

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
