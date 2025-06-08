package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

@KryoType
public record TileSelectionData(int x, int y, boolean selected) {
}
