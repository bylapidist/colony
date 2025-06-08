package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoType;

/**
 * Wrapper object persisted to disk containing save metadata and map state.
 */
@KryoType
public record SaveData(int version, int kryoHash, MapState mapState) {
}
