package net.lapidist.colony.components.maps;

import net.lapidist.colony.components.AbstractBoundedComponent;

public final class TileComponent extends AbstractBoundedComponent {

    private boolean passable;

    private boolean selected;

    private boolean dirty;

    private String tileTypeId;

    public boolean isPassable() {
        return passable;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getTileType() {
        return tileTypeId;
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

    public void setTileType(final String tileTypeToSet) {
        this.tileTypeId = tileTypeToSet;
    }
}
