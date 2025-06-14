package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message for gathering resources from a tile.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param resourceType type of resource to gather
 */
@KryoType
public record ResourceGatherRequestData(int x, int y, String resourceType) {
    public ResourceGatherRequestData() {
        this(0, 0, null);
    }
}
