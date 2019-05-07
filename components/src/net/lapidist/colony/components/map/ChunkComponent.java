package net.lapidist.colony.components.map;

import com.artemis.Component;

public class ChunkComponent extends Component {

    private int[][] tiles;

    public ChunkComponent() {
        this(32, 32);
    }

    public ChunkComponent(int chunkWidth, int chunkHeight) {
        tiles = new int[chunkWidth][chunkHeight];
    }

    public int[][] getTiles() {
        return tiles;
    }

    public int getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
    }

    public void setTile(int x, int y, int tile) {
        this.tiles[x][y] = tile;
    }
}
