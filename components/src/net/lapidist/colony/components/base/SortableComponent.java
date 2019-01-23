package net.lapidist.colony.components.base;

import com.artemis.Component;

public class SortableComponent extends Component {

    private int layer = 0;

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
