package net.lapidist.colony.components.resources;

import com.artemis.Component;

/**
 * Holds resource amounts available on a tile.
 */
public final class ResourceComponent extends Component {
    private int wood;
    private int stone;
    private int food;
    private boolean dirty;

    public int getWood() {
        return wood;
    }

    public void setWood(final int woodToSet) {
        this.wood = woodToSet;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(final int stoneToSet) {
        this.stone = stoneToSet;
    }

    public int getFood() {
        return food;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setFood(final int foodToSet) {
        this.food = foodToSet;
    }
}
