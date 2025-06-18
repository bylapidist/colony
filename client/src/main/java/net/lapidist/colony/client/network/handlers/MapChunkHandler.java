package net.lapidist.colony.client.network.handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import net.lapidist.colony.components.state.map.MapChunkBytes;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

/**
 * Receives compressed map chunk data and triggers a callback with the
 * deserialized {@link MapChunkData}.
 */
public final class MapChunkHandler extends AbstractMessageHandler<MapChunkBytes> {
    private final Consumer<MapChunkData> consumer;
    private final Kryo kryo;

    public MapChunkHandler(final Consumer<MapChunkData> consumerToUse, final Kryo kryoInstance) {
        super(MapChunkBytes.class);
        this.consumer = consumerToUse;
        this.kryo = kryoInstance;
    }

    @Override
    public void handle(final MapChunkBytes message) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data());
            try (GZIPInputStream gzip = new GZIPInputStream(bais); Input input = new Input(gzip)) {
                MapChunkData chunk = kryo.readObject(input, MapChunkData.class);
                consumer.accept(chunk);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode chunk", e);
        }
    }
}
