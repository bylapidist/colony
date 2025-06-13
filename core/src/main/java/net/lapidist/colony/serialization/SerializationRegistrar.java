package net.lapidist.colony.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.RecordSerializer;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.MapMetadata;
import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.save.SaveData;

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
            if (type.isRecord()) {
                kryo.register(type, new RecordSerializer(type));
            } else {
                kryo.register(type);
            }
        }
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
        kryo.register(java.util.HashMap.class);
        kryo.register(java.util.Map.class);
    }

    /**
     * Computes a hash of the registered class names. This can be stored in a save
     * file to detect format drift when registrations change.
     */
    public static int registrationHash() {
        StringBuilder builder = new StringBuilder();
        for (Class<?> type : REGISTERED_TYPES) {
            builder.append(type.getName()).append(';');
        }
        return builder.toString().hashCode();
    }

    private static final Class<?>[] REGISTERED_TYPES = {
            TileData.class,
            TileSelectionData.class,
            MapState.class,
            BuildingData.class,
            BuildingPlacementData.class,
            BuildingRemovalData.class,
            ResourceData.class,
            net.lapidist.colony.components.resources.ResourceType.class,
            ResourceGatherRequestData.class,
            ResourceUpdateData.class,
            MapMetadata.class,
            MapChunk.class,
            TilePos.class,
            net.lapidist.colony.chat.ChatMessage.class,
            SaveData.class
    };
}
