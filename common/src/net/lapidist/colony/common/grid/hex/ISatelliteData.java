package net.lapidist.colony.common.grid.hex;

public interface ISatelliteData {

    boolean isPassable();

    void setPassable(boolean passable);

    boolean isOpaque();

    void setOpaque(boolean opaque);

    float getMovementCost();

    void setMovementCost(float movementCost);
}
