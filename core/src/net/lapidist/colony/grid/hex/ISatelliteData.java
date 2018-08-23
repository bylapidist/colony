package net.lapidist.colony.grid.hex;

public interface ISatelliteData {

    boolean isPassable();

    void setPassable(boolean passable);

    boolean isOpaque();

    void setOpaque(boolean opaque);

    float getMovementCost();

    void setMovementCost(float movementCost);
}
