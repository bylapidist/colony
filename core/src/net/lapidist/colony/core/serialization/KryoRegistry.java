package net.lapidist.colony.core.serialization;

import com.esotericsoftware.kryo.Kryo;
import net.lapidist.colony.serialization.SerializationRegistrar;

public final class KryoRegistry {

    private KryoRegistry() { }

    public static void register(final Kryo kryo) {
        SerializationRegistrar.register(kryo);
    }
}
