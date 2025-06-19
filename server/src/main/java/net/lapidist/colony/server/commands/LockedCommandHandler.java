package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.map.MapState;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base handler that acquires a {@link ReentrantLock} before modifying the state.
 * Subclasses implement {@link #modify(Object, MapState)} to perform their changes.
 *
 * @param <C> command type
 */
public abstract class LockedCommandHandler<C extends ServerCommand> implements CommandHandler<C> {

    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;
    private final ReentrantLock lock;

    protected LockedCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                   final Consumer<MapState> stateConsumerToUse,
                                   final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.lock = lockToUse;
    }

    @Override
    public final void handle(final C command) {
        lock.lock();
        try {
            MapState state = stateSupplier.get();
            MapState updated = modify(command, state);
            if (updated != null) {
                stateConsumer.accept(updated);
            }
            afterUpdate(command, updated != null ? updated : state);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Apply the command to the supplied state.
     *
     * @param command command to process
     * @param state   current map state
     * @return new state instance, or {@code null} if unchanged
     */
    protected abstract MapState modify(C command, MapState state);

    /**
     * Hook invoked after the state has been consumed.
     * Subclasses may override to perform side effects like broadcasting events.
     */
    protected void afterUpdate(final C command, final MapState newState) {
        // default no-op
    }
}
