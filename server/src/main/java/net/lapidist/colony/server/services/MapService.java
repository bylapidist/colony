package net.lapidist.colony.server.services;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.map.chunk.ChunkGenerator;
import net.lapidist.colony.components.state.MapChunk;
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
    private final ChunkGenerator chunkGenerator;
    private final String saveName;
    private MapState state;

    public MapService(final MapGenerator generator, final ChunkGenerator chunkGen, final String name) {
        this.mapGenerator = generator;
        this.chunkGenerator = chunkGen;
        this.saveName = name;
    }

    public MapState load() throws IOException {
        Path saveFile = Paths.get().getAutosave(saveName);
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
        this.state = state;
        return state;
    }

    private MapState generateMap() {
        return mapGenerator.generate(
                GameConstants.MAP_CHUNK_SIZE,
                GameConstants.MAP_CHUNK_SIZE
        );
    }

    /**
     * Generates or retrieves a chunk at the given coordinates.
     */
    public MapChunk loadChunk(final int chunkX, final int chunkY) {
        MapChunk chunk = chunkGenerator.generate(chunkX, chunkY, GameConstants.MAP_CHUNK_SIZE);
        state.tiles().putAll(chunk.tiles());
        return chunk;
    }
}
