package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoType;

/**
 * Wrapper object persisted to disk containing save metadata and map state.
 *
 * @param version  the save format version
 * @param kryoHash hash used to validate serializer configuration
 * @param mapState the game state snapshot
 */
@KryoType
public record SaveData(int version, int kryoHash, MapState mapState) {
}
