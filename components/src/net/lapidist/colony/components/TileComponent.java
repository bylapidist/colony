package net.lapidist.colony.components;

import com.artemis.Component;

public class TileComponent extends Component {

    private int width;
    private int height;

    public final void setTile(final int widthToSet, final int heightToSet) {
        setWidth(widthToSet);
        setHeight(heightToSet);
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(final int widthToSet) {
        this.width = widthToSet;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(final int heightToSet) {
        this.height = heightToSet;
    }
}
