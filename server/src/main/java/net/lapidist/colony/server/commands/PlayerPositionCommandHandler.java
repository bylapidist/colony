package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.PlayerPosition;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link PlayerPositionCommand} to the map state. */
public final class PlayerPositionCommandHandler extends LockedCommandHandler<PlayerPositionCommand> {

    public PlayerPositionCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                        final Consumer<MapState> stateConsumerToUse,
                                        final ReentrantLock lockToUse) {
        super(stateSupplierToUse, stateConsumerToUse, lockToUse);
    }

    @Override
    public Class<PlayerPositionCommand> type() {
        return PlayerPositionCommand.class;
    }

    @Override
    protected MapState modify(final PlayerPositionCommand command, final MapState state) {
        return state.toBuilder()
                .playerPos(new PlayerPosition(command.x(), command.y()))
                .build();
    }
}
