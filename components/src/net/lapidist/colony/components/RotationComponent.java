package net.lapidist.colony.components;

import com.artemis.Component;

public class RotationComponent extends Component {

    private float rotation = 0f;

    public final float getRotation() {
        return rotation;
    }

    public final void setRotation(final float rotationToSet) {
        this.rotation = rotationToSet;
    }
}
