package net.lapidist.colony.components.maps;

import net.lapidist.colony.components.AbstractBoundedComponent;

public class TileComponent extends AbstractBoundedComponent {

    public enum TileType {
        EMPTY("empty"),
        DIRT("dirt"),
        GRASS("grass");

        private final String type;

        TileType(final String typeToSet) {
            this.type = typeToSet;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private boolean passable;

    private boolean selected;

    private TileType tileType;

    public final boolean isPassable() {
        return passable;
    }

    public final boolean isSelected() {
        return selected;
    }

    public final TileType getTileType() {
        return tileType;
    }

    public final void setPassable(final boolean passableToSet) {
        this.passable = passableToSet;
    }

    public final void setSelected(final boolean selectedToSet) {
        this.selected = selectedToSet;
    }

    public final void setTileType(final TileType tileTypeToSet) {
        this.tileType = tileTypeToSet;
    }
}
