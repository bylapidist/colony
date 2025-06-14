package net.lapidist.colony.map;

/**
 * Preset map sizes selectable when creating a new game.
 */
public enum MapSize {
    SMALL(100, 100),
    MEDIUM(150, 150),
    LARGE(200, 200),
    VERY_LARGE(250, 250);

    private final int width;
    private final int height;

    MapSize(final int w, final int h) {
        this.width = w;
        this.height = h;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
