package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.entities.BuildingComponent;

/**
 * Command representing a building placement request.
 *
 * @param x    tile x coordinate
 * @param y    tile y coordinate
 * @param type building type
 */
public record BuildCommand(int x, int y, BuildingComponent.BuildingType type) implements ServerCommand {
}
