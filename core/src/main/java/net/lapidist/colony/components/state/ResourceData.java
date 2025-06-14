package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable resource amounts attached to a tile.
 */
@KryoType
public record ResourceData(Map<String, Integer> amounts) {

    public ResourceData() {
        this(Map.of());
    }

    public ResourceData(final int wood, final int stone, final int food) {
        this(Map.of(
                "WOOD", wood,
                "STONE", stone,
                "FOOD", food
        ));
    }

    /**
     * Amount of the specified resource id.
     *
     * @param id resource identifier
     * @return stored amount or {@code 0}
     */
    public int amount(final String id) {
        return amounts.getOrDefault(id, 0);
    }

    /**
     * Return a copy of this data with the given amount replaced.
     *
     * @param id  resource identifier
     * @param amt new amount
     * @return updated immutable instance
     */
    public ResourceData with(final String id, final int amt) {
        Map<String, Integer> map = new HashMap<>(amounts);
        map.put(id, amt);
        return new ResourceData(Map.copyOf(map));
    }

    /**
     * Convenience accessor for wood.
     */
    public int wood() {
        return amount("WOOD");
    }

    /**
     * Convenience accessor for stone.
     */
    public int stone() {
        return amount("STONE");
    }

    /**
     * Convenience accessor for food.
     */
    public int food() {
        return amount("FOOD");
    }
}
