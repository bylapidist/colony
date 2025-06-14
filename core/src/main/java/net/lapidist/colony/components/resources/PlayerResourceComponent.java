package net.lapidist.colony.components.resources;

import com.artemis.Component;

/** Stores player resource amounts. */
public final class PlayerResourceComponent extends Component {
    private final java.util.Map<String, Integer> amounts = new java.util.HashMap<>();

    public int getWood() {
        return getAmount("WOOD");
    }

    public void setWood(final int amount) {
        setAmount("WOOD", amount);
    }

    public void addWood(final int amount) {
        addAmount("WOOD", amount);
    }

    public int getStone() {
        return getAmount("STONE");
    }

    public void setStone(final int amount) {
        setAmount("STONE", amount);
    }

    public void addStone(final int amount) {
        addAmount("STONE", amount);
    }

    public int getFood() {
        return getAmount("FOOD");
    }

    public void setFood(final int amount) {
        setAmount("FOOD", amount);
    }

    public void addFood(final int amount) {
        addAmount("FOOD", amount);
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

    public void setAmount(final String id, final int amt) {
        amounts.put(id, amt);
    }

    public void addAmount(final String id, final int delta) {
        amounts.merge(id, delta, Integer::sum);
    }
}
