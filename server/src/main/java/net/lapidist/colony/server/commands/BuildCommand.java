package net.lapidist.colony.server.commands;

/**
 * Command representing a building placement request.
 *
 * @param x    tile x coordinate
 * @param y    tile y coordinate
 * @param type building type identifier
 */
public record BuildCommand(int x, int y, String type) implements ServerCommand {
}
