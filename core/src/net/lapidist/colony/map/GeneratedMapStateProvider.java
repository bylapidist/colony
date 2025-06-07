package net.lapidist.colony.map;

import net.lapidist.colony.components.state.MapState;

/**
 * Generates a new {@link MapState} using a {@link MapGenerator}.
 */
public final class GeneratedMapStateProvider implements MapStateProvider {
    private final MapGenerator mapGenerator;
    private final int width;
    private final int height;

    public GeneratedMapStateProvider(
            final MapGenerator mapGeneratorToSet,
            final int widthToSet,
            final int heightToSet
    ) {
        this.mapGenerator = mapGeneratorToSet;
        this.width = widthToSet;
        this.height = heightToSet;
    }

    @Override
    public MapState provide() {
        return mapGenerator.generate(width, height);
    }
}
