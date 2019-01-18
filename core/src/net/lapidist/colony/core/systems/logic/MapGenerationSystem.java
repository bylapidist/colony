package net.lapidist.colony.core.systems.logic;

import com.artemis.BaseSystem;
import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.MapLayout;
import net.lapidist.colony.common.map.tile.ITileGrid;

public class MapGenerationSystem extends BaseSystem {

    private ITileGrid grid;

    public MapGenerationSystem(int width, int height, int ppm) {
        this.grid = new MapBuilder()
                .setGridHeight(width)
                .setGridWidth(height)
                .setTileWidth(ppm)
                .setTileHeight(ppm)
                .setMapLayout(MapLayout.RECTANGULAR)
                .build();
    }

    public ITileGrid getGrid() {
        return grid;
    }

    @Override
    protected void processSystem() {

    }
}
