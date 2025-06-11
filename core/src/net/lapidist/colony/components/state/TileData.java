package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Immutable tile representation used in {@link MapState}.
 *
 * @param x          tile x coordinate
 * @param y          tile y coordinate
 * @param tileType   tile terrain type identifier
 * @param passable   whether units can move over the tile
 * @param selected   whether the tile is currently selected
 * @param resources  resources contained on the tile
 */
@KryoType
public record TileData(
        int x,
        int y,
        String tileType,
        boolean passable,
        boolean selected,
        ResourceData resources
) {

    public TileData() {
        this(0, 0, null, false, false, new ResourceData());
    }

    /**
     * Create a builder prepopulated with this tile's values.
     *
     * @return builder instance
     */
    public Builder toBuilder() {
        return new Builder(this);
    }

    /**
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link TileData} allowing mutation before construction.
     */
    public static final class Builder {
        private int x;
        private int y;
        private String tileType;
        private boolean passable;
        private boolean selected;
        private ResourceData resources;

        private Builder() { }

        private Builder(final TileData data) {
            this.x = data.x;
            this.y = data.y;
            this.tileType = data.tileType;
            this.passable = data.passable;
            this.selected = data.selected;
            this.resources = data.resources;
        }

        public Builder x(final int newX) {
            this.x = newX;
            return this;
        }

        public Builder y(final int newY) {
            this.y = newY;
            return this;
        }

        public Builder tileType(final String newTileType) {
            this.tileType = newTileType;
            return this;
        }

        public Builder passable(final boolean newPassable) {
            this.passable = newPassable;
            return this;
        }

        public Builder selected(final boolean newSelected) {
            this.selected = newSelected;
            return this;
        }

        public Builder resources(final ResourceData newResources) {
            this.resources = newResources;
            return this;
        }

        /**
         * Construct a new immutable {@link TileData} instance.
         */
        public TileData build() {
            return new TileData(x, y, tileType, passable, selected, resources);
        }
    }
}
