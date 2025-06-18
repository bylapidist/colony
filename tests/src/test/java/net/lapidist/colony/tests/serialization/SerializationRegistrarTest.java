package net.lapidist.colony.tests.serialization;

import com.esotericsoftware.kryo.Kryo;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.serialization.SerializationRegistrar;
import net.lapidist.colony.mod.ModMetadata;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SerializationRegistrarTest {
    @Test
    public void registersAnnotatedClasses() {
        Kryo kryo = new Kryo();
        SerializationRegistrar.register(kryo);
        assertTrue(kryo.getRegistration(MapState.class) != null);
        assertTrue(kryo.getRegistration(ModMetadata.class) != null);
    }
}
