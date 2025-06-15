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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class GameStateIOVersionTest {

    @Test
    public void throwsWhenVersionIsNewer() throws Exception {
        Path file = Files.createTempFile("state", ".dat");
        MapState state = new MapState()
                .toBuilder()
                .version(SaveVersion.CURRENT.number() + 1)
                .build();
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(Files.newOutputStream(file))) {
            SaveData data = new SaveData(
                    SaveVersion.CURRENT.number() + 1,
                    SerializationRegistrar.registrationHash(),
                    state
            );
            kryo.writeObject(output, data);
        }

        boolean thrown = false;
        try {
            GameStateIO.load(file);
        } catch (IOException e) {
            thrown = true;
        }
        Files.deleteIfExists(file);
        assertTrue(thrown);
    }
}
