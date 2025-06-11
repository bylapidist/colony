package net.lapidist.colony.components;

import com.artemis.Component;

/**
 * Base class for components that define a rectangular area.
 */
public abstract class AbstractBoundedComponent extends Component implements BoundedComponent {
    private int x;
    private int y;
    private int width;
    private int height;

    public final int getX() {
        return x;
    }

    @Override
    public final void setX(final int xToSet) {
        this.x = xToSet;
    }

    public final int getY() {
        return y;
    }

    @Override
    public final void setY(final int yToSet) {
        this.y = yToSet;
    }

    public final int getWidth() {
        return width;
    }

    @Override
    public final void setWidth(final int widthToSet) {
        this.width = widthToSet;
    }

    public final int getHeight() {
        return height;
    }

    @Override
    public final void setHeight(final int heightToSet) {
        this.height = heightToSet;
    }
}
