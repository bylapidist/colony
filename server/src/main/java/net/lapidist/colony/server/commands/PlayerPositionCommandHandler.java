package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.PlayerPosition;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link PlayerPositionCommand} to the map state. */
public final class PlayerPositionCommandHandler implements CommandHandler<PlayerPositionCommand> {
    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;
    private final ReentrantLock lock;

    public PlayerPositionCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                        final Consumer<MapState> stateConsumerToUse,
                                        final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.lock = lockToUse;
    }

    @Override
    public Class<PlayerPositionCommand> type() {
        return PlayerPositionCommand.class;
    }

    @Override
    public void handle(final PlayerPositionCommand command) {
        lock.lock();
        try {
            MapState state = stateSupplier.get();
            MapState updated = state.toBuilder()
                    .playerPos(new PlayerPosition(command.x(), command.y()))
                    .build();
            stateConsumer.accept(updated);
        } finally {
            lock.unlock();
        }
    }
}
