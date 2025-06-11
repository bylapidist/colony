package net.lapidist.colony.server.commands;

/**
 * Command representing a tile selection toggle.
 *
 * @param x        tile x coordinate
 * @param y        tile y coordinate
 * @param selected whether the tile should be selected
 */
public record TileSelectionCommand(int x, int y, boolean selected) implements ServerCommand {
}
