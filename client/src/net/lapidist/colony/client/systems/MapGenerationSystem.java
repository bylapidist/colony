package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.MapFactory;
import net.lapidist.colony.map.DefaultMapGenerator;
import net.lapidist.colony.map.MapGenerator;

public final class MapGenerationSystem extends BaseSystem {

    private final int mapWidth;

    private final int mapHeight;

    private final MapGenerator mapGenerator;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this(new DefaultMapGenerator(), mapWidthToSet, mapHeightToSet);
    }

    public MapGenerationSystem(
            final MapGenerator mapGeneratorToSet,
            final int mapWidthToSet,
            final int mapHeightToSet
    ) {
        this.mapGenerator = mapGeneratorToSet;
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    public void initialize() {
        var state = mapGenerator.generate(mapWidth, mapHeight);
        MapFactory.create(world, state);
    }

    @Override
    protected void processSystem() {
        // map generation occurs once in initialize
    }

}
