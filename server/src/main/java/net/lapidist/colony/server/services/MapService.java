package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.components.state.CameraPosition;
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
    private final int width;
    private final int height;

    public MapService(final MapGenerator generator, final String name, final int mapWidth, final int mapHeight) {
        this.mapGenerator = generator;
        this.saveName = name;
        this.width = mapWidth;
        this.height = mapHeight;
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
        int alignedWidth = (int) Math.ceil(width / (double) MapChunkData.CHUNK_SIZE)
                * MapChunkData.CHUNK_SIZE;
        int alignedHeight = (int) Math.ceil(height / (double) MapChunkData.CHUNK_SIZE)
                * MapChunkData.CHUNK_SIZE;
        MapState state = mapGenerator.generate(alignedWidth, alignedHeight);
        return state.toBuilder()
                .width(width)
                .height(height)
                .playerPos(new PlayerPosition(alignedWidth / 2, alignedHeight / 2))
                .cameraPos(new CameraPosition(
                        alignedWidth / 2f,
                        alignedHeight / 2f
                ))
                .build();
    }
}
