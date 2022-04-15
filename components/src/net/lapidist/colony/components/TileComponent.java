package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;

public class TileComponent implements Component {

    private int x;

    private int y;

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final void setX(final int xToSet) {
        this.x = xToSet;
    }

    public final void setY(final int yToSet) {
        this.y = yToSet;
    }
}
