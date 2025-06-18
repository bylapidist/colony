package net.lapidist.colony.components.state.resources;

import net.lapidist.colony.serialization.KryoType;

/**
 * Broadcast message containing updated resources for a tile.
 *
 * @param x     tile x coordinate
 * @param y     tile y coordinate
 * @param amounts map of resource identifiers to updated amounts
 */
@KryoType
public record ResourceUpdateData(int x, int y, java.util.Map<String, Integer> amounts) {
    public ResourceUpdateData() {
        this(0, 0, new java.util.HashMap<>());
    }
}
