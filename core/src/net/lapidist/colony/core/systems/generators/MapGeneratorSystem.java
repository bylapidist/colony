package net.lapidist.colony.core.systems.generators;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import net.lapidist.colony.core.systems.builders.TerrainBuilderSystem;

@Wire
public class MapGeneratorSystem extends BaseSystem {

    private TerrainBuilderSystem terrainBuilderSystem;

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int chunkWidth;
    private int chunkHeight;
    private int[][] chunkMap;

    public MapGeneratorSystem(int width, int height, int tileWidth, int tileHeight, int chunkWidth, int chunkHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.chunkMap = new int[width][height];
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void processSystem() {
    }

    private int generateChunk(int x, int y) {
        return terrainBuilderSystem.createChunk(x, y);
    }

    public void generate() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                chunkMap[tx][ty] = generateChunk(tx, ty);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getChunkWidth() {
        return chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }
}
