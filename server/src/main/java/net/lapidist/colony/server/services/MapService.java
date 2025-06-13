package net.lapidist.colony.server.services;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.server.io.GameStateIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads existing maps or generates new ones when no save exists.
 */
public final class MapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapService.class);

    private final MapGenerator mapGenerator;
    private final String saveName;

    public MapService(final MapGenerator generator, final String name) {
        this.mapGenerator = generator;
        this.saveName = name;
    }

    public MapState load() throws IOException {
        Path saveFile = Paths.get().getAutosave(saveName);
        MapState state;
        if (Files.exists(saveFile)) {
            state = GameStateIO.load(saveFile);
            LOGGER.info("Loaded save file: {}", saveFile);
        } else {
            state = generateMap();
            GameStateIO.save(state, saveFile);
            LOGGER.info("Generated new map and saved to: {}", saveFile);
        }
        state = state.toBuilder()
                .saveName(saveName)
                .autosaveName(saveName + Paths.AUTOSAVE_SUFFIX)
                .build();
        Files.writeString(Paths.get().getLastAutosaveMarker(), saveName);
        return state;
    }

    private MapState generateMap() {
        int width = (int) Math.ceil(GameConstants.MAP_WIDTH / (double) MapChunkData.CHUNK_SIZE)
                * MapChunkData.CHUNK_SIZE;
        int height = (int) Math.ceil(GameConstants.MAP_HEIGHT / (double) MapChunkData.CHUNK_SIZE)
                * MapChunkData.CHUNK_SIZE;
        return mapGenerator.generate(width, height);
    }
}
