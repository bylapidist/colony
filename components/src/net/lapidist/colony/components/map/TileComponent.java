package net.lapidist.colony.components.map;

import com.artemis.Component;

public class TileComponent extends Component {

    private int width;
    private int height;

    public void setTile(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
