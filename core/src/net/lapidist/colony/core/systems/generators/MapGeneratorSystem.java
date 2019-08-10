package net.lapidist.colony.core.systems.generators;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;

import static com.artemis.E.E;

@Wire
public class MapGeneratorSystem extends BaseSystem {

    private EntityFactorySystem entityFactorySystem;
    private MapAssetSystem assetSystem;
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
        int chunk = entityFactorySystem.create(entityFactorySystem.getArchetype("chunk"));
        E(chunk).originComponentOrigin(new Vector2(x, y));
        E(chunk).worldPositionComponentPosition(new Vector3(x * getTileWidth(), y * getTileHeight(), 0));
        generateTerrain(chunk);

        return chunk;
    }

    private void generateTerrain(int chunk) {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("terrain"));

        E(e).textureComponentTexture(assetSystem.getTexture("grass"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                E(chunk).worldPositionComponentPosition().x,
                E(chunk).worldPositionComponentPosition().y,
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).sortableComponentLayer(0);
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
