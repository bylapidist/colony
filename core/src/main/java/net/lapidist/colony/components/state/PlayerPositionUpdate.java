package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Message sent by the client whenever the player changes tile position.
 */
@KryoType
public record PlayerPositionUpdate(int x, int y) { }
