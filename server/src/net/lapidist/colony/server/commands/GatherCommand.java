package net.lapidist.colony.server.commands;

/**
 * Command representing a resource gather request.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param resourceType type of resource to gather
 */
public record GatherCommand(int x, int y, String resourceType) implements ServerCommand {
}
