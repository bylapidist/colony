package net.lapidist.colony.tests.map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.serialization.KryoRegistry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class MapStateChunkSerializationTest {

    @Test
    public void roundTripsWithChunks() {
        MapState state = new MapState();
        state.putTile(TileData.builder().x(1).y(2).tileType("GRASS").passable(true).build());
        Kryo kryo = new Kryo();
        KryoRegistry.register(kryo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Output output = new Output(baos)) {
            kryo.writeObject(output, state);
        }
        MapState result;
        try (Input input = new Input(new ByteArrayInputStream(baos.toByteArray()))) {
            result = kryo.readObject(input, MapState.class);
        }
        assertEquals("GRASS", result.getTile(1, 2).tileType());
    }
}
