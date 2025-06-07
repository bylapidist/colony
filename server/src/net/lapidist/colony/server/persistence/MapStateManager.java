package net.lapidist.colony.server.persistence;

import net.lapidist.colony.components.state.MapState;

import java.io.File;
import java.io.IOException;

public final class MapStateManager {
    private final GameStateSerializer<MapState> serializer;
    private final File saveFile;

    public MapStateManager(final GameStateSerializer<MapState> serializerToSet, final File saveFileToSet) {
        this.serializer = serializerToSet;
        this.saveFile = saveFileToSet;
    }

    public MapState loadOrCreate() {
        try {
            if (saveFile.exists()) {
                return serializer.deserialize(saveFile);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        MapState generated = MapGenerator.generate();
        try {
            serializer.serialize(generated, saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generated;
    }
}
