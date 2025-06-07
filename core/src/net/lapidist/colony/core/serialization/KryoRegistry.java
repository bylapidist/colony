package net.lapidist.colony.core.serialization;

import com.esotericsoftware.kryo.Kryo;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TileSelectionData;

public final class KryoRegistry {

    private KryoRegistry() { }

    public static void register(final Kryo kryo) {
        kryo.register(MapState.class);
        kryo.register(TileData.class);
        kryo.register(BuildingData.class);
        kryo.register(TileSelectionData.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
    }
}
