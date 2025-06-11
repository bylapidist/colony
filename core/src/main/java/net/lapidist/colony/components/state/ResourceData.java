package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Immutable resource amounts attached to a tile.
 *
 * @param wood  wood amount
 * @param stone stone amount
 * @param food  food amount
 */
@KryoType
public record ResourceData(int wood, int stone, int food) {
    public ResourceData() {
        this(0, 0, 0);
    }
}
