package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class OriginComponent extends Component {

    private Vector2 origin;

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
}
