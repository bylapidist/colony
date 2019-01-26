package net.lapidist.colony.components.base;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent extends Component {

    private Vector2 velocity;

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
