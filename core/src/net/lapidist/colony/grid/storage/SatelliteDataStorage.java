package net.lapidist.colony.grid.storage;

import net.lapidist.colony.grid.hex.ISatelliteData;

public class SatelliteDataStorage implements ISatelliteData {

    private boolean passable;
    private boolean opaque;
    private float movementCost;

    @Override
    public boolean isPassable() {
        return passable;
    }

    @Override
    public void setPassable(final boolean passable) {
        this.passable = passable;
    }

    @Override
    public boolean isOpaque() {
        return opaque;
    }

    @Override
    public void setOpaque(final boolean opaque) {
        this.opaque = opaque;
    }

    @Override
    public float getMovementCost() {
        return movementCost;
    }

    @Override
    public void setMovementCost(final float movementCost) {
        this.movementCost = movementCost;
    }
}
