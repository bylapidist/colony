package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/** Request message for gathering resources from a tile. */
@KryoType
public record ResourceGatherRequestData(int x, int y, String resourceType) { }
