package net.lapidist.colony.server.io;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public final class GameStateIO {

    private GameStateIO() { }

    public static void save(final MapState state, final Path file) throws IOException {
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(new FileOutputStream(file.toFile()))) {
            SaveData data = new SaveData(
                    SaveVersion.CURRENT.number(),
                    SerializationRegistrar.registrationHash(),
                    state
            );
            kryo.writeObject(output, data);
        }
    }

    public static MapState load(final Path file) throws IOException {
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Input input = new Input(new FileInputStream(file.toFile()))) {
            SaveData data = kryo.readObject(input, SaveData.class);
            SaveFormatValidator validator = new DefaultSaveFormatValidator();
            validator.validate(data);
            SaveMigrationStrategy migrator = new DefaultSaveMigrationStrategy();
            return migrator.migrate(data);
        }
    }

    // Class registration handled by KryoRegistry
}
