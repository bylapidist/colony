package net.lapidist.colony.components.render;

import com.artemis.Component;

public class RenderableComponent extends Component {

    private boolean renderable;

    public RenderableComponent() {
        renderable = true;
    }

    public RenderableComponent(boolean renderable) {
        this();

        setRenderable(renderable);
    }

    public boolean isRenderable() {
        return renderable;
    }

    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }
}
