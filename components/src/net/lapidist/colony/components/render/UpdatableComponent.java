package net.lapidist.colony.components.render;

import com.artemis.Component;

public class UpdatableComponent extends Component {

    private boolean updatable;

    public UpdatableComponent() {
        updatable = true;
    }

    public UpdatableComponent(boolean renderable) {
        this();

        setUpdatable(renderable);
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }
}
