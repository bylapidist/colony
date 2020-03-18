package net.lapidist.colony.core.systems.generators;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.factories.LightFactorySystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;

import static com.artemis.E.E;

@Wire
public class MapGeneratorSystem extends BaseSystem {

    private static final float ORIGIN_OFFSET = 0.5f;

    private EntityFactorySystem entityFactorySystem;
    private LightFactorySystem lightFactorySystem;
    private MapPhysicsSystem physicsSystem;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int chunkWidth;
    private int chunkHeight;
    private int[][] chunkMap;

    public MapGeneratorSystem(
            final int widthToSet,
            final int heightToSet,
            final int tileWidthToSet,
            final int tileHeightToSet,
            final int chunkWidthToSet,
            final int chunkHeightToSet
    ) {
        this.width = widthToSet;
        this.height = heightToSet;
        this.tileWidth = tileWidthToSet;
        this.tileHeight = tileHeightToSet;
        this.chunkWidth = chunkWidthToSet;
        this.chunkHeight = chunkHeightToSet;
        this.chunkMap = new int[widthToSet][heightToSet];
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void processSystem() {
    }

    private int generateChunk(final int x, final int y) {
        int chunk = entityFactorySystem.create(
                entityFactorySystem.getArchetype("chunk")
        );
        E(chunk).originComponentOrigin(new Vector2(x, y));
        E(chunk).worldPositionComponentPosition(
                new Vector3(
                        x * getTileWidth(),
                        y * getTileHeight(),
                        0
                )
        );
        generateTerrain(chunk);

        return chunk;
    }

    private void generateTerrain(final int chunk) {
        int e = entityFactorySystem.create(
                entityFactorySystem.getArchetype("terrain")
        );

//        E(e).textureComponentTexture(assetSystem.getTexture("grass"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(ORIGIN_OFFSET, ORIGIN_OFFSET));
        E(e).worldPositionComponentPosition(new Vector3(
                E(chunk).worldPositionComponentPosition().x,
                E(chunk).worldPositionComponentPosition().y,
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).sortableComponentLayer(0);
    }

    public final void generate() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                chunkMap[tx][ty] = generateChunk(tx, ty);
            }
        }
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final int getTileWidth() {
        return tileWidth;
    }

    public final int getTileHeight() {
        return tileHeight;
    }

    public final int getChunkWidth() {
        return chunkWidth;
    }

    public final int getChunkHeight() {
        return chunkHeight;
    }
}
