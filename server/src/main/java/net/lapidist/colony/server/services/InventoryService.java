package net.lapidist.colony.server.services;

import net.lapidist.colony.registry.Registries;

import java.util.HashMap;
import java.util.Map;

/** Simple service tracking collected item amounts. */
public final class InventoryService {
    private final Map<String, Integer> amounts = new HashMap<>();

    /**
     * Add the specified amount of an item to the inventory if the item exists in the registry.
     *
     * @param itemId item identifier
     * @param amount quantity to add
     */
    public void addItem(final String itemId, final int amount) {
        if (itemId == null || amount == 0 || Registries.items().get(itemId) == null) {
            return;
        }
        amounts.merge(itemId, amount, Integer::sum);
    }

    /**
     * Current amount stored for the given item id.
     */
    public int getAmount(final String itemId) {
        return amounts.getOrDefault(itemId, 0);
    }

    /**
     * @return copy of all stored item amounts
     */
    public Map<String, Integer> getItems() {
        return new HashMap<>(amounts);
    }
}
