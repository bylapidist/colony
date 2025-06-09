package net.lapidist.colony.components.resources;

import com.artemis.Component;

/** Stores player resource amounts. */
public final class PlayerResourceComponent extends Component {
    private int wood;
    private int stone;
    private int food;

    public int getWood() {
        return wood;
    }

    public void addWood(final int amount) {
        this.wood += amount;
    }

    public int getStone() {
        return stone;
    }

    public void addStone(final int amount) {
        this.stone += amount;
    }

    public int getFood() {
        return food;
    }

    public void addFood(final int amount) {
        this.food += amount;
    }
}
