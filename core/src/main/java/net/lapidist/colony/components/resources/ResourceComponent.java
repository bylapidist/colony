package net.lapidist.colony.components.resources;

import com.artemis.Component;

/**
 * Holds resource amounts available on a tile.
 */
public final class ResourceComponent extends Component {
    private final java.util.Map<String, Integer> amounts = new java.util.HashMap<>();
    private boolean dirty;

    public int getWood() {
        return getAmount("WOOD");
    }

    public void setWood(final int woodToSet) {
        setAmount("WOOD", woodToSet);
    }

    public int getStone() {
        return getAmount("STONE");
    }

    public void setStone(final int stoneToSet) {
        setAmount("STONE", stoneToSet);
    }

    public int getFood() {
        return getAmount("FOOD");
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setFood(final int foodToSet) {
        setAmount("FOOD", foodToSet);
    }

    public java.util.Map<String, Integer> getAmounts() {
        return amounts;
    }

    public void setAmounts(final java.util.Map<String, Integer> newAmounts) {
        amounts.clear();
        amounts.putAll(newAmounts);
    }

    public int getAmount(final String id) {
        return amounts.getOrDefault(id, 0);
    }

    public void setAmount(final String id, final int value) {
        amounts.put(id, value);
    }
}
