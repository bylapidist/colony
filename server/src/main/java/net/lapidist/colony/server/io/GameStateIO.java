package net.lapidist.colony.server.io;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;
import net.lapidist.colony.mod.ModMetadata;
import java.util.List;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public final class GameStateIO {

    private GameStateIO() { }

    public static void save(
            final MapState state,
            final List<ModMetadata> mods,
            final Path file) throws IOException {
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(new FileOutputStream(file.toFile()))) {
            SaveData data = new SaveData(
                    SaveVersion.CURRENT.number(),
                    SerializationRegistrar.registrationHash(),
                    state,
                    mods
            );
            kryo.writeObject(output, data);
        }
    }

    public static void save(final MapState state, final Path file) throws IOException {
        save(state, new java.util.ArrayList<>(), file);
    }

    public static SaveData loadData(final Path file) throws IOException {
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Input input = new Input(new FileInputStream(file.toFile()))) {
            SaveData data = kryo.readObject(input, SaveData.class);
            SaveFormatValidator validator = new DefaultSaveFormatValidator();
            validator.validate(data);
            SaveMigrationStrategy migrator = new DefaultSaveMigrationStrategy();
            MapState state = migrator.migrate(data);
            return new SaveData(data.version(), data.kryoHash(), state, data.mods());
        }
    }

    public static MapState load(final Path file) throws IOException {
        return loadData(file).mapState();
    }

    /**
     * Reads only the map dimensions from the supplied autosave file.
     */
    public static AutosaveMetadata readMetadata(final Path file) throws IOException {
        MapState state = load(file);
        return new AutosaveMetadata(state.width(), state.height());
    }

    // Class registration handled by KryoRegistry
}
