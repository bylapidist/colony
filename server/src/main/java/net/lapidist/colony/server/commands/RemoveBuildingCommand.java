package net.lapidist.colony.server.commands;

/** Command representing a building removal request. */
public record RemoveBuildingCommand(int x, int y) implements ServerCommand {
}
