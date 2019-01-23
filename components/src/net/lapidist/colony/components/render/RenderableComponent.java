package net.lapidist.colony.components.render;

import com.artemis.Component;

public class RenderableComponent extends Component {

    private int layer = 0;

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
