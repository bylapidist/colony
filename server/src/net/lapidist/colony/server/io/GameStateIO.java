package net.lapidist.colony.server.io;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public final class GameStateIO {

    private GameStateIO() { }

    public static void save(final MapState state, final Path file) throws IOException {
        Kryo kryo = new Kryo();
        register(kryo);
        try (Output output = new Output(new FileOutputStream(file.toFile()))) {
            kryo.writeObject(output, state);
        }
    }

    public static MapState load(final Path file) throws IOException {
        Kryo kryo = new Kryo();
        register(kryo);
        try (Input input = new Input(new FileInputStream(file.toFile()))) {
            MapState state = kryo.readObject(input, MapState.class);
            if (state.getVersion() > MapState.CURRENT_VERSION) {
                throw new IOException(
                        String.format(
                                "Unsupported map version %d (current %d)",
                                state.getVersion(),
                                MapState.CURRENT_VERSION
                        )
                );
            }
            return state;
        }
    }

    private static void register(final Kryo kryo) {
        kryo.register(MapState.class);
        kryo.register(TileData.class);
        kryo.register(BuildingData.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
    }
}
