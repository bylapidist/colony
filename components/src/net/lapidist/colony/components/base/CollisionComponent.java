package net.lapidist.colony.components.base;

import com.artemis.Component;

public class CollisionComponent extends Component {

    private boolean colliding;

    public boolean isColliding() {
        return colliding;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
}
