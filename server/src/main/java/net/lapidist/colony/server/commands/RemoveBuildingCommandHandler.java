package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.BuildingRemovedEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link RemoveBuildingCommand} to the game state and broadcasts the change. */
public final class RemoveBuildingCommandHandler implements CommandHandler<RemoveBuildingCommand> {
    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;
    private final NetworkService networkService;
    private final ReentrantLock lock;

    public RemoveBuildingCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                        final Consumer<MapState> stateConsumerToUse,
                                        final NetworkService networkServiceToUse,
                                        final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.networkService = networkServiceToUse;
        this.lock = lockToUse;
    }

    @Override
    public Class<RemoveBuildingCommand> type() {
        return RemoveBuildingCommand.class;
    }

    @Override
    public void handle(final RemoveBuildingCommand command) {
        lock.lock();
        try {
            MapState state = stateSupplier.get();
            Iterator<BuildingData> it = state.buildings().iterator();
            BuildingData target = null;
            while (it.hasNext()) {
                BuildingData bd = it.next();
                if (bd.x() == command.x() && bd.y() == command.y()) {
                    target = bd;
                    it.remove();
                    break;
                }
            }
            if (target == null) {
                return;
            }
            stateConsumer.accept(state);
            Events.dispatch(new BuildingRemovedEvent(command.x(), command.y()));
            networkService.broadcast(new BuildingRemovalData(command.x(), command.y()));
        } finally {
            lock.unlock();
        }
    }
}
