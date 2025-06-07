package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.MapFactory;
import net.lapidist.colony.map.MapGenerator;

public final class MapGenerationSystem extends BaseSystem {

    private final int mapWidth;

    private final int mapHeight;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    public void initialize() {
        var state = MapGenerator.generate(mapWidth, mapHeight);
        MapFactory.create(world, state);
    }

    @Override
    protected void processSystem() {
        // map generation occurs once in initialize
    }

}
