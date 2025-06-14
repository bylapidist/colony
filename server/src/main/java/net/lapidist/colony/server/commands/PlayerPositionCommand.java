package net.lapidist.colony.server.commands;

/** Command representing a player position update. */
public record PlayerPositionCommand(int x, int y) implements ServerCommand {
}
