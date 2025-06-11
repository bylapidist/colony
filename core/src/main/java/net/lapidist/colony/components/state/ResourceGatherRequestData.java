package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;
import net.lapidist.colony.components.resources.ResourceType;

/**
 * Request message for gathering resources from a tile.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param resourceType type of resource to gather
 */
@KryoType
public record ResourceGatherRequestData(int x, int y, ResourceType resourceType) { }
