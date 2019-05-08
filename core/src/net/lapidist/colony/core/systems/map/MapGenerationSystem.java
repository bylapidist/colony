package net.lapidist.colony.core.systems.map;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
    private int tileWidth;
    private int tileHeight;
    private int[][] chunkMap;

    public MapGenerationSystem(int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
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
        Rectangle bounds = new Rectangle(
                E(chunk).worldPositionComponentPosition().x,
                E(chunk).worldPositionComponentPosition().y,
                getTileWidth() * getWidth(),
                getTileHeight() * getTileHeight()
        );

        for (float ty = bounds.getY(); ty < bounds.getY() + bounds.getHeight(); ty += getTileHeight()) {
            for (float tx = bounds.getX(); tx < bounds.getX() + bounds.getWidth(); tx += getTileWidth()) {
                int e = entityFactorySystem.create(entityFactorySystem.getArchetype("terrain"));

                E(e).textureComponentTexture(assetSystem.getTexture("grass"));
                E(e).rotationComponentRotation(0);
                E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
                E(e).worldPositionComponentPosition(new Vector3(
                        tx,
                        ty,
                        0
                ));
                E(e).scaleComponentScale(1);
                E(e).sortableComponentLayer(0);
            }
        }
    }

    void generate() {
        for (int ty = 0; ty < getHeight(); ty++) {
            for (int tx = 0; tx < getWidth(); tx++) {
                chunkMap[tx][ty] = generateChunk(tx, ty);
            }
        }

        createPlayer();
    }

    private int createPlayer() {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("player"));
        E(e).textureComponentTexture(assetSystem.getTexture("dirt"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                (getWidth() / 2f) * getTileWidth(),
                (getHeight() / 2f) * getTileHeight(),
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
}
