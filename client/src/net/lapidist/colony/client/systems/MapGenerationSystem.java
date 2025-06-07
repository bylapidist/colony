package net.lapidist.colony.client.systems;

import net.lapidist.colony.map.DefaultMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.map.GeneratedMapStateProvider;

/**
 * System that generates a map on initialization using a {@link MapGenerator}.
 */

public final class MapGenerationSystem extends MapInitSystem {

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
        super(new GeneratedMapStateProvider(mapGeneratorToSet, mapWidthToSet, mapHeightToSet));
        this.mapGenerator = mapGeneratorToSet;
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

}
