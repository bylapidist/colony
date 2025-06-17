package net.lapidist.colony.tests.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;
import net.lapidist.colony.save.io.GameStateIO;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class GameStateIOMigrationV35Test {

    @Test
    public void migratesV35ToCurrent() throws Exception {
        Path file = Files.createTempFile("state", ".dat");
        MapState state = MapState.builder()
                .version(SaveVersion.V35.number())
                .build();
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(Files.newOutputStream(file))) {
            SaveData data = new SaveData(
                    SaveVersion.V35.number(),
                    SerializationRegistrar.registrationHash(),
                    state
            );
            kryo.writeObject(output, data);
        }

        MapState loaded = GameStateIO.load(file);
        Files.deleteIfExists(file);
        assertEquals(SaveVersion.CURRENT.number(), loaded.version());
        assertNotNull(loaded.inventory());
    }
}
