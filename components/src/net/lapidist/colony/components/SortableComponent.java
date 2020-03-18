package net.lapidist.colony.components;

import com.artemis.Component;

public class SortableComponent extends Component {

    private int layer = 0;

    public final int getLayer() {
        return layer;
    }

    public final void setLayer(final int layerToSet) {
        this.layer = layerToSet;
    }
}
