package net.lapidist.colony.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.RecordSerializer;
import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.messages.TileSelectionData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.components.state.messages.BuildingPlacementData;
import net.lapidist.colony.components.state.messages.BuildingRemovalData;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.resources.ResourceGatherRequestData;
import net.lapidist.colony.components.state.resources.ResourceUpdateData;
import net.lapidist.colony.components.state.map.MapMetadata;
import net.lapidist.colony.components.state.map.MapChunk;
import net.lapidist.colony.components.state.map.MapChunkBytes;
import net.lapidist.colony.components.state.messages.MapChunkRequest;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.components.state.map.PlayerPosition;
import net.lapidist.colony.components.state.map.CameraPosition;
import net.lapidist.colony.components.state.messages.PlayerPositionUpdate;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.Season;

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
        kryo.register(java.util.concurrent.ConcurrentHashMap.class);
        com.esotericsoftware.kryo.serializers.ImmutableCollectionsSerializers.registerSerializers(kryo);
        kryo.register(java.util.Map.class);
        kryo.register(byte[].class);
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

    /** Precomputed registration hash for quick access. */
    public static final int REGISTRATION_HASH = 1361101144;

    private static final Class<?>[] REGISTERED_TYPES = {
            TileData.class,
            TileSelectionData.class,
            MapState.class,
            BuildingData.class,
            BuildingPlacementData.class,
            BuildingRemovalData.class,
            ResourceData.class,
            net.lapidist.colony.registry.ResourceDefinition.class,
            net.lapidist.colony.registry.ResourceRegistry.class,
            ResourceGatherRequestData.class,
            ResourceUpdateData.class,
            MapMetadata.class,
            MapChunk.class,
            MapChunkBytes.class,
            MapChunkRequest.class,
            net.lapidist.colony.components.state.map.ChunkPos.class,
            net.lapidist.colony.map.MapChunkData.class,
            TilePos.class,
            net.lapidist.colony.network.ChatMessage.class,
            PlayerPosition.class,
            SaveData.class,
            CameraPosition.class,
            PlayerPositionUpdate.class,
            ModMetadata.class,
            EnvironmentState.class,
            Season.class
    };
}
