package net.lapidist.colony.server.services;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.io.GameStateIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles loading existing saves or generating new maps.
 */
public final class MapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapService.class);

    private final String saveName;
    private final MapGenerator mapGenerator;

    private MapState mapState;

    public MapService(final String saveNameToUse, final MapGenerator generator) {
        this.saveName = saveNameToUse;
        this.mapGenerator = generator;
    }

    /**
     * Loads an existing map if present or generates a new one.
     *
     * @return the resulting map state
     */
    public MapState loadOrGenerate() throws IOException {
        Path saveFile = Paths.getAutosave(saveName);
        if (Files.exists(saveFile)) {
            mapState = GameStateIO.load(saveFile);
            LOGGER.info("Loaded save file: {}", saveFile);
        } else {
            generateMap();
            GameStateIO.save(mapState, saveFile);
            LOGGER.info("Generated new map and saved to: {}", saveFile);
        }
        mapState = mapState.withSaveName(saveName);
        mapState = mapState.withAutosaveName(saveName + Paths.AUTOSAVE_SUFFIX);
        Files.writeString(Paths.getLastAutosaveMarker(), saveName);
        return mapState;
    }

    private void generateMap() {
        mapState = mapGenerator.generate(
                GameConstants.MAP_WIDTH,
                GameConstants.MAP_HEIGHT
        );
    }

    public MapState getMapState() {
        return mapState;
    }
}
