package net.lapidist.colony.server.commands;

/** Command representing a resource gather request. */
public record GatherCommand(int x, int y, String resourceType) implements ServerCommand {
}
