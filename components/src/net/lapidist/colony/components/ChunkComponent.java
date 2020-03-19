package net.lapidist.colony.components;

import com.artemis.Component;

public class ChunkComponent extends Component {

    private int[][] tiles;

    public ChunkComponent() {
        tiles = new int[0][0];
    }

    public ChunkComponent(final int chunkWidth, final int chunkHeight) {
        tiles = new int[chunkWidth][chunkHeight];
    }

    public final int[][] getTiles() {
        return tiles;
    }

    public final void setTiles(final int[][] tilesToSet) {
        this.tiles = tilesToSet;
    }

    public final int getTile(final int x, final int y) {
        return tiles[x][y];
    }

    public final void setTile(final int x, final int y, final int tile) {
        this.tiles[x][y] = tile;
    }
}
