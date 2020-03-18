package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class OriginComponent extends Component {

    private Vector2 origin;

    public final Vector2 getOrigin() {
        return origin;
    }

    public final void setOrigin(final Vector2 originToSet) {
        this.origin = originToSet;
    }
}
