package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent extends Component {

    private Vector2 velocity;

    public final Vector2 getVelocity() {
        return velocity;
    }

    public final void setVelocity(final Vector2 velocityToSet) {
        this.velocity = velocityToSet;
    }
}
