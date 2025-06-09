package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.entities.BuildingComponent;

/**
 * Command representing a building placement request.
 */
public record BuildCommand(int x, int y, BuildingComponent.BuildingType type) implements ServerCommand {
}
