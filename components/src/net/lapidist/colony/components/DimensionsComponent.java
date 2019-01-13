package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class DimensionsComponent extends Component {

    private Vector3 dimensions;

    public DimensionsComponent() {
        this.dimensions = new Vector3(0, 0, 0);
    }

    public DimensionsComponent(Vector3 dimensions) {
        this.dimensions = dimensions;
    }

    public Vector3 getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector3 dimensions) {
        this.dimensions = dimensions;
    }
}
