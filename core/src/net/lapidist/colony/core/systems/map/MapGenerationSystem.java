package net.lapidist.colony.core.systems.map;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;

import static com.artemis.E.E;

@Wire
public class MapGenerationSystem extends BaseSystem {

    private EntityFactorySystem entityFactorySystem;
    private MapAssetSystem assetSystem;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int[][] map;

    public MapGenerationSystem(int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.map = new int[width][height];
    }

    @Override
    protected void initialize() {
        for (int ty = 0; ty < height; ty++) {
            for (int tx = 0; tx < width; tx++) {
                map[tx][ty] = entityFactorySystem.create(entityFactorySystem.getArchetype("tile"));

                E(map[tx][ty]).tileComponentTile(tileWidth, tileHeight);
                E(map[tx][ty]).positionComponentPosition(new Vector3(tx, ty, 0));
                E(map[tx][ty]).originComponentOrigin(new Vector2(0.5f, 0.5f));
            }
        }
    }

    @Override
    protected void processSystem() {

    }

    public void generate() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                int e = entityFactorySystem.create(entityFactorySystem.getArchetype("terrain"));

                E(e).textureComponentTexture(assetSystem.getTexture("dirt"));
                E(e).rotationComponentRotation(0);
                E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
                E(e).positionComponentPosition(new Vector3(
                        tx * getTileWidth(),
                        ty * getTileHeight(),
                        0
                ));
                E(e).scaleComponentScale(1);
                E(e).sortableComponentLayer(0);
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

    public int[][] getMap() {
        return map;
    }
}
