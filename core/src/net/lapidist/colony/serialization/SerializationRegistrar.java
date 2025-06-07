package net.lapidist.colony.serialization;

import com.esotericsoftware.kryo.Kryo;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Registers all classes annotated with {@link KryoType} with a given Kryo instance.
 */
public final class SerializationRegistrar {
    private SerializationRegistrar() { }

    /**
     * Registers annotated classes with the supplied Kryo instance.
     *
     * @param kryo the Kryo instance to register classes with
     */
    public static void register(final Kryo kryo) {
        Reflections reflections = new Reflections("net.lapidist.colony");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(KryoType.class);
        for (Class<?> type : types) {
            kryo.register(type);
        }
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
    }
}
