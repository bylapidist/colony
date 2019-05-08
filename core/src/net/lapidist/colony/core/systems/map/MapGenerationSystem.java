package net.lapidist.colony.core.systems.map;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.factories.LightFactorySystem;
import net.lapidist.colony.core.systems.generators.TerrainGeneratorSystem;

import static com.artemis.E.E;

@Wire
public class MapGenerationSystem extends BaseSystem {

    private EntityFactorySystem entityFactorySystem;
    private LightFactorySystem lightFactorySystem;
    private MapAssetSystem assetSystem;
    private MapPhysicsSystem physicsSystem;
    private TerrainGeneratorSystem terrainGeneratorSystem;
    private int width;
    private int height;
    private int chunkWidth;
    private int chunkHeight;
    private int[][] chunkMap;

    public MapGenerationSystem(int width, int height, int chunkWidth, int chunkHeight) {
        this.width = width;
        this.height = height;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.chunkMap = new int[width][height];
    }

    @Override
    protected void initialize() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                chunkMap[tx][ty] = generateChunk(tx, ty);
            }
        }
    }

    @Override
    protected void processSystem() {

    }

    private int generateChunk(int x, int y) {
        int chunk = entityFactorySystem.create(entityFactorySystem.getArchetype("chunk"));
        E(chunk).originComponentOrigin(new Vector2(x, y));
        E(chunk).worldPositionComponentPosition(new Vector3(x * getChunkWidth(), y * getChunkHeight(), 0));

        for (int ty = 0; ty < getChunkWidth(); ty++) {
            for (int tx = 0; tx < getChunkHeight(); tx++) {
                int tile = entityFactorySystem.create(entityFactorySystem.getArchetype("tile"));
                E(tile).tileComponentTile(Constants.PPM, Constants.PPM);
                E(tile).worldPositionComponentPosition(new Vector3(tx, ty, 0));
                E(tile).originComponentOrigin(new Vector2(0.5f, 0.5f));

                E(chunk).chunkComponentTile(tx, ty, tile);
            }
        }

        return chunk;
    }

    void generate() {
        createTerrain();
        createPlayer();
    }

    private int createPlayer() {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("player"));
        E(e).textureComponentTexture(assetSystem.getTexture("dirt"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                (getWidth() / 2f) * getChunkWidth(),
                (getHeight() / 2f) * getChunkHeight(),
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).velocityComponentVelocity(new Vector2(0, 0));
        E(e).sortableComponentLayer(1);
        E(e).dynamicBodyComponentFixtureDef().shape = new CircleShape();
        E(e).dynamicBodyComponentFixtureDef().shape.setRadius(0.5f);
        E(e).dynamicBodyComponentBodyDef().position.set(
                E(e).worldPositionComponentPosition().x,
                E(e).worldPositionComponentPosition().y
        );
        E(e).dynamicBodyComponentBody(
                physicsSystem.getPhysicsWorld().createBody(
                        E(e).dynamicBodyComponentBodyDef()
                )
        );
        E(e).dynamicBodyComponentBody().createFixture(
                E(e).dynamicBodyComponentFixtureDef()
        );

        PointLight light = lightFactorySystem.createPointlight(
                physicsSystem.getRayHandler(),
                E(e).dynamicBodyComponentBody(),
                new Color(1, 1, 1, 0.4f),
                10
        );

        ConeLight coneLight = lightFactorySystem.createConeLight(
                physicsSystem.getRayHandler(),
                E(e).dynamicBodyComponentBody(),
                new Color(1, 1, 1, 0.5f),
                20,
                E(e).rotationComponentRotation(),
                30
        );

        E(e).pointLightComponentPointLights().add(light);
        E(e).coneLightComponentConeLights().add(coneLight);

        return e;
    }

    private void createTerrain() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                int e = entityFactorySystem.create(entityFactorySystem.getArchetype("terrain"));

                E(e).textureComponentTexture(assetSystem.getTexture("grass"));
                E(e).rotationComponentRotation(0);
                E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
                E(e).worldPositionComponentPosition(new Vector3(
                        tx * E(chunkMap[tx][ty]).originComponentOrigin().x,
                        ty * E(chunkMap[tx][ty]).originComponentOrigin().y,
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

    public int getChunkWidth() {
        return chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }
}
