package net.lapidist.colony.components.base;

import com.artemis.Component;

public class RotationComponent extends Component {

    private float rotation = 0f;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
