package net.lapidist.colony.components;

import com.artemis.Component;

public class ScaleComponent extends Component {

    private float scale = 1f;

    public final float getScale() {
        return scale;
    }

    public final void setScale(final float scaleToSet) {
        this.scale = scaleToSet;
    }
}
