package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.resources.ResourceType;

/**
 * Command representing a resource gather request.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param resourceType type of resource to gather
 */
public record GatherCommand(int x, int y, ResourceType resourceType) implements ServerCommand {
}
