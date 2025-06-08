package net.lapidist.colony.components;

/**
 * Interface for components that define a rectangular area.
 */
public interface BoundedComponent {
    void setX(int x);

    void setY(int y);

    void setWidth(int width);

    void setHeight(int height);
}
