package net.lapidist.colony.serialization;

import com.esotericsoftware.kryo.Kryo;

public final class KryoRegistry {

    private KryoRegistry() { }

    public static void register(final Kryo kryo) {
        SerializationRegistrar.register(kryo);
    }
}
