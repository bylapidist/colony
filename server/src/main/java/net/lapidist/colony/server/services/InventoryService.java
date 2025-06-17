package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.registry.Registries;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Simple service tracking collected item amounts persisted in {@link MapState}. */
public final class InventoryService {
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final ReentrantLock lock;

    public InventoryService(final Supplier<MapState> stateSupplier,
                            final Consumer<MapState> stateConsumer,
                            final ReentrantLock lockToUse) {
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.lock = lockToUse;
    }

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
        lock.lock();
        try {
            MapState state = supplier.get();
            Map<String, Integer> inv = new HashMap<>(state.inventory());
            inv.merge(itemId, amount, Integer::sum);
            consumer.accept(state.toBuilder().inventory(inv).build());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Current amount stored for the given item id.
     */
    public int getAmount(final String itemId) {
        lock.lock();
        try {
            return supplier.get().inventory().getOrDefault(itemId, 0);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return copy of all stored item amounts
     */
    public Map<String, Integer> getItems() {
        lock.lock();
        try {
            return new HashMap<>(supplier.get().inventory());
        } finally {
            lock.unlock();
        }
    }
}
