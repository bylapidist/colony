package net.lapidist.colony.tests.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.map.ChunkPos;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GameStateIOMigrationV6Test {

    @Test
    public void migratesV6ToCurrent() throws Exception {
        Path file = Files.createTempFile("state", ".dat");
        Map<TilePos, TileData> tiles = new HashMap<>();
        tiles.put(new TilePos(0, 0), TileData.builder().x(0).y(0).tileType("GRASS").passable(true).build());
        @SuppressWarnings("unchecked")
        Map<ChunkPos, MapChunkData> misTyped = (Map<ChunkPos, MapChunkData>) (Map<?, ?>) tiles;
        MapState state = MapState.builder()
                .version(SaveVersion.V6.number())
                .chunks(misTyped)
                .build();
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        try (Output output = new Output(Files.newOutputStream(file))) {
            SaveData data = new SaveData(
                    SaveVersion.V6.number(),
                    SerializationRegistrar.registrationHash(),
                    state
            );
            kryo.writeObject(output, data);
        }

        MapState loaded = GameStateIO.load(file);
        Files.deleteIfExists(file);
        assertEquals(SaveVersion.CURRENT.number(), loaded.version());
        assertEquals("grass", loaded.getTile(0, 0).tileType());
    }
}
