package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.messages.BuildingRemovalData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.BuildingRemovedEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link RemoveBuildingCommand} to the game state and broadcasts the change. */
public final class RemoveBuildingCommandHandler extends LockedCommandHandler<RemoveBuildingCommand> {
    private final NetworkService networkService;

    public RemoveBuildingCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                        final Consumer<MapState> stateConsumerToUse,
                                        final NetworkService networkServiceToUse,
                                        final ReentrantLock lockToUse) {
        super(stateSupplierToUse, stateConsumerToUse, lockToUse);
        this.networkService = networkServiceToUse;
    }

    @Override
    public Class<RemoveBuildingCommand> type() {
        return RemoveBuildingCommand.class;
    }

    @Override
    protected MapState modify(final RemoveBuildingCommand command, final MapState state) {
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
            return null;
        }
        Events.dispatch(new BuildingRemovedEvent(command.x(), command.y()));
        networkService.broadcast(new BuildingRemovalData(command.x(), command.y()));
        return state;
    }
}
