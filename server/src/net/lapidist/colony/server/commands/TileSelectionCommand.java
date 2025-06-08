package net.lapidist.colony.server.commands;

/**
 * Command representing a tile selection toggle.
 */
public record TileSelectionCommand(int x, int y, boolean selected) implements ServerCommand {
}
