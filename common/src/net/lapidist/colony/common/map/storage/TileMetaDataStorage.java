package net.lapidist.colony.common.map.storage;

import net.lapidist.colony.common.map.tile.ITileMetaData;

public class TileMetaDataStorage implements ITileMetaData {

    private boolean passable;
    private boolean opaque;
    private float movementCost;

    @Override
    public boolean isPassable() {
        return passable;
    }

    @Override
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    @Override
    public boolean isOpaque() {
        return opaque;
    }

    @Override
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    @Override
    public float getMovementCost() {
        return movementCost;
    }

    @Override
    public void setMovementCost(float movementCost) {
        this.movementCost = movementCost;
    }
}
