package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

@KryoType
public record BuildingData(int x, int y, String buildingType, String textureRef) {
}
