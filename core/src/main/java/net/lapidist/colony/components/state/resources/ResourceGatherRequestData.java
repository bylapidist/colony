package net.lapidist.colony.components.state.resources;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message for gathering resources from a tile.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param resourceId  identifier of the resource to gather
 */
@KryoType
public record ResourceGatherRequestData(int x, int y, String resourceId) {
    public ResourceGatherRequestData() {
        this(0, 0, null);
    }
}
