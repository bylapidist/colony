package net.lapidist.colony.common.map.tile;

public interface ITileMetaData {

    boolean isPassable();

    void setPassable(boolean passable);

    boolean isOpaque();

    void setOpaque(boolean opaque);

    float getMovementCost();

    void setMovementCost(float movementCost);
}
