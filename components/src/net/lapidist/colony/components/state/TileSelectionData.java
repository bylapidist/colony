package net.lapidist.colony.components.state;

public final class TileSelectionData {
    private int x;
    private int y;
    private boolean selected;

    public int getX() {
        return x;
    }

    public void setX(final int xToSet) {
        this.x = xToSet;
    }

    public int getY() {
        return y;
    }

    public void setY(final int yToSet) {
        this.y = yToSet;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selectedToSet) {
        this.selected = selectedToSet;
    }
}
