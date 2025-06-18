package net.lapidist.colony.tests.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;
import net.lapidist.colony.save.io.GameStateIO;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class GameStateIOMigrationV12Test {

    @Test
    public void migratesV12ToCurrent() throws Exception {
        Path file = Files.createTempFile("state", ".dat");
        MapState state = MapState.builder()
                .version(SaveVersion.V12.number())
                .build();
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(Files.newOutputStream(file))) {
            SaveData data = new SaveData(
                    SaveVersion.V12.number(),
                    SerializationRegistrar.registrationHash(),
                    state
            );
            kryo.writeObject(output, data);
        }

        MapState loaded = GameStateIO.load(file);
        Files.deleteIfExists(file);
        assertEquals(SaveVersion.CURRENT.number(), loaded.version());
    }
}
