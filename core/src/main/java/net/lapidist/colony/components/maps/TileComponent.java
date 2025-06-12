package net.lapidist.colony.components.maps;

import net.lapidist.colony.components.AbstractBoundedComponent;

public final class TileComponent extends AbstractBoundedComponent {

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

    private boolean dirty;

    private TileType tileType;

    public boolean isPassable() {
        return passable;
    }

    public boolean isSelected() {
        return selected;
    }

    public TileType getTileType() {
        return tileType;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setPassable(final boolean passableToSet) {
        this.passable = passableToSet;
    }

    public void setSelected(final boolean selectedToSet) {
        this.selected = selectedToSet;
    }

    public void setTileType(final TileType tileTypeToSet) {
        this.tileType = tileTypeToSet;
    }
}
