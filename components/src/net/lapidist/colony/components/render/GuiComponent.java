package net.lapidist.colony.components.render;

import com.artemis.Component;

public class GuiComponent extends Component {

    private boolean renderable;

    public GuiComponent() {
        renderable = true;
    }

    public GuiComponent(boolean renderable) {
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
