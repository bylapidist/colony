package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/** Broadcast message containing updated resources for a tile. */
@KryoType
public record ResourceUpdateData(int x, int y, int wood, int stone, int food) { }
