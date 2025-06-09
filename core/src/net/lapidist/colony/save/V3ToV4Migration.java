package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;

/** Migration from save version 3 to version 4 adding resource data. */
public final class V3ToV4Migration implements MapStateMigration {
    private static final int DEFAULT_WOOD = 10;
    private static final int DEFAULT_STONE = 5;
    private static final int DEFAULT_FOOD = 3;
    @Override
    public int fromVersion() {
        return SaveVersion.V3.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V4.number();
    }

    @Override
    public MapState apply(final MapState state) {
        for (TilePos pos : state.tiles().keySet()) {
            TileData tile = state.tiles().get(pos);
            if (tile.resources() == null) {
                TileData updated = tile.toBuilder()
                        .resources(new ResourceData(DEFAULT_WOOD, DEFAULT_STONE, DEFAULT_FOOD))
                        .build();
                state.tiles().put(pos, updated);
            }
        }
        return state.toBuilder().version(toVersion()).build();
    }
}
