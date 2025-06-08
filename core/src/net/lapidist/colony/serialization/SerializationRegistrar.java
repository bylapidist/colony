package net.lapidist.colony.serialization;

import com.esotericsoftware.kryo.Kryo;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TileSelectionData;

/**
 * Registers all serializable classes with a given Kryo instance.
 */
public final class SerializationRegistrar {
    private SerializationRegistrar() { }

    /**
     * Registers known classes with the supplied Kryo instance.
     *
     * @param kryo the Kryo instance to register classes with
     */
    public static void register(final Kryo kryo) {
        for (Class<?> type : REGISTERED_TYPES) {
            kryo.register(type);
        }
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
    }

    private static final Class<?>[] REGISTERED_TYPES = {
            TileData.class,
            TileSelectionData.class,
            MapState.class,
            BuildingData.class
    };
}
