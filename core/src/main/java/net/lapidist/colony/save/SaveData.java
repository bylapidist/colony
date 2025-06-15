package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoType;
import net.lapidist.colony.mod.ModMetadata;
import java.util.List;

/**
 * Wrapper object persisted to disk containing save metadata and map state.
 *
 * @param version  the save format version
 * @param kryoHash hash used to validate serializer configuration
 * @param mapState the game state snapshot
 * @param mods     list of loaded mod metadata
 */
@KryoType
public record SaveData(int version, int kryoHash, MapState mapState, List<ModMetadata> mods) {

    public SaveData {
        mods = mods == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(mods);
    }

    public SaveData(final int version, final int kryoHash, final MapState mapState) {
        this(version, kryoHash, mapState, new java.util.ArrayList<>());
    }
}
