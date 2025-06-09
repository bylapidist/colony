package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.BuildingPlacedEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.function.Supplier;

/**
 * Applies a {@link BuildCommand} to the game state and broadcasts the change.
 */
public final class BuildCommandHandler implements CommandHandler<BuildCommand> {
    private final Supplier<MapState> stateSupplier;
    private final NetworkService networkService;

    public BuildCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final NetworkService networkServiceToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.networkService = networkServiceToUse;
    }

    @Override
    public Class<BuildCommand> type() {
        return BuildCommand.class;
    }

    @Override
    public void handle(final BuildCommand command) {
        MapState state = stateSupplier.get();
        TileData tile = state.tiles().get(new TilePos(command.x(), command.y()));
        boolean occupied = state.buildings().stream()
                .anyMatch(b -> b.x() == command.x() && b.y() == command.y());
        if (tile == null || occupied) {
            return;
        }
        String texture = defaultTexture(command.type());
        BuildingData building = new BuildingData(command.x(), command.y(), command.type().name(), texture);
        state.buildings().add(building);
        Events.dispatch(new BuildingPlacedEvent(command.x(), command.y(), command.type().name()));
        networkService.broadcast(building);
    }

    private static String defaultTexture(final BuildingComponent.BuildingType type) {
        return switch (type) {
            case HOUSE -> "house0";
            case MARKET -> "house0";
            case FACTORY -> "house0";
        };
    }
}
