package net.lapidist.colony.components.entities;

import com.artemis.Component;

/** Component holding the player's world position. */
public final class PlayerComponent extends Component {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(final float xToSet) {
        this.x = xToSet;
    }

    public float getY() {
        return y;
    }

    public void setY(final float yToSet) {
        this.y = yToSet;
    }
}
