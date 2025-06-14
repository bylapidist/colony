package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
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

    public MapService(final MapGenerator generator, final int widthToSet, final int heightToSet, final String name) {
        this.mapGenerator = generator;
        this.width = widthToSet;
        this.height = heightToSet;
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
        MapState state = mapGenerator.generate(width, height);
        return state.toBuilder()
                .playerPos(new PlayerPosition(width / 2, height / 2))
                .cameraPos(new net.lapidist.colony.components.state.CameraPosition(width / 2f, height / 2f))
                .width(width)
                .height(height)
                .build();
    }
}
