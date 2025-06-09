package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message sent by clients when placing a building.
 */
@KryoType
public record BuildingPlacementData(int x, int y, String buildingType) {
}
