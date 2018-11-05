package net.lapidist.colony.common.grid;

import net.lapidist.colony.common.grid.hex.HexagonOrientation;
import net.lapidist.colony.common.grid.hex.IHexagonalGrid;
import net.lapidist.colony.common.grid.implementation.GridImplementation;
import net.lapidist.colony.common.grid.storage.HexagonDataStorage;
import net.lapidist.colony.common.grid.storage.IHexagonDataStorage;
import net.lapidist.colony.common.grid.strategy.GridLayoutStrategy;
import net.lapidist.colony.common.grid.hex.ISatelliteData;

public class GridBuilder<T extends ISatelliteData> {

    private int gridWidth;
    private int gridHeight;
    private float radius;
    private IHexagonDataStorage hexagonDataStorage = new HexagonDataStorage();
    private HexagonOrientation orientation = HexagonOrientation.POINTY_TOP;
    private GridLayout gridLayout = GridLayout.RECTANGULAR;

    public IHexagonalGrid<T> build() {
        return new GridImplementation<>(this);
    }

    public float getRadius() {
        return radius;
    }

    public GridBuilder setRadius(final float radius) {
        this.radius = radius;
        return this;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public GridBuilder setGridWidth(final int gridWidth) {
        this.gridWidth = gridWidth;
        return this;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public GridBuilder setGridHeight(final int gridHeight) {
        this.gridHeight = gridHeight;
        return this;
    }

    public HexagonOrientation getOrientation() {
        return orientation;
    }

    public GridBuilder setOrientation(final HexagonOrientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayout.getGridLayoutStrategy();
    }

    public IHexagonDataStorage<T> getHexagonDataStorage() {
        return hexagonDataStorage;
    }

    public GridData getGridData() {
        return new GridData(orientation, gridLayout, radius, gridWidth, gridHeight);
    }

    public GridBuilder setGridLayout(final GridLayout gridLayout) {
        this.gridLayout = gridLayout;
        return this;
    }
}
