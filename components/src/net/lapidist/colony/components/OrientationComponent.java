package net.lapidist.colony.components;

import com.artemis.Component;
import net.lapidist.colony.common.map.tile.TileOrientation;

public class OrientationComponent extends Component {

    private TileOrientation orientation;

    public OrientationComponent() {
    }

    public OrientationComponent(TileOrientation orientation) {
        this.orientation = orientation;
    }

    public TileOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(TileOrientation orientation) {
        this.orientation = orientation;
    }
}
