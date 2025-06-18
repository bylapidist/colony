package net.lapidist.colony.client.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.entities.PlayerComponent;

/**
 * Maintains Box2D bodies representing occluders for lights.
 */
public final class LightOcclusionSystem extends BaseSystem implements Disposable {

    private final IntMap<Body> buildingBodies = new IntMap<>();
    private Body playerBody;
    private World physicsWorld;

    private ComponentMapper<BuildingComponent> buildingMapper;
    private ComponentMapper<PlayerComponent> playerMapper;

    public LightOcclusionSystem(final World world) {
        this.physicsWorld = world;
    }

    /** Update the Box2D world used for bodies. */
    public void setWorld(final World world) {
        this.physicsWorld = world;
    }

    @Override
    protected void processSystem() {
        if (physicsWorld == null) {
            return;
        }
        updateBuildings();
        updatePlayer();
    }

    private void updateBuildings() {
        IntBag ids = world.getAspectSubscriptionManager()
                .get(Aspect.all(BuildingComponent.class))
                .getEntities();
        int[] data = ids.getData();
        for (int i = 0, s = ids.size(); i < s; i++) {
            int id = data[i];
            if (!buildingBodies.containsKey(id)) {
                Entity e = world.getEntity(id);
                BuildingComponent bc = buildingMapper.get(e);
                buildingBodies.put(id, createBuildingBody(bc));
            }
        }
        IntMap.Keys keys = buildingBodies.keys();
        while (keys.hasNext) {
            int id = keys.next();
            if (!contains(ids, id)) {
                Body body = buildingBodies.remove(id);
                if (body != null) {
                    physicsWorld.destroyBody(body);
                }
            }
        }
    }

    private void updatePlayer() {
        IntBag players = world.getAspectSubscriptionManager()
                .get(Aspect.all(PlayerComponent.class))
                .getEntities();
        if (players.isEmpty()) {
            return;
        }
        Entity player = world.getEntity(players.get(0));
        PlayerComponent pc = playerMapper.get(player);
        if (playerBody == null) {
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.DynamicBody;
            playerBody = physicsWorld.createBody(def);
            PolygonShape shape = new PolygonShape();
            float half = GameConstants.TILE_SIZE / 2f;
            shape.setAsBox(half, half);
            playerBody.createFixture(shape, 0f);
            shape.dispose();
        }
        playerBody.setTransform(pc.getX(), pc.getY(), 0f);
    }

    private Body createBuildingBody(final BuildingComponent bc) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        float half = GameConstants.TILE_SIZE / 2f;
        def.position.set(bc.getX() * GameConstants.TILE_SIZE + half,
                bc.getY() * GameConstants.TILE_SIZE + half);
        Body body = physicsWorld.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(half, half);
        body.createFixture(shape, 0f);
        shape.dispose();
        return body;
    }

    private static boolean contains(final IntBag bag, final int value) {
        int[] arr = bag.getData();
        for (int i = 0, s = bag.size(); i < s; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        if (physicsWorld == null) {
            return;
        }
        IntMap.Values<Body> bodies = buildingBodies.values();
        while (bodies.hasNext()) {
            physicsWorld.destroyBody(bodies.next());
        }
        buildingBodies.clear();
        if (playerBody != null) {
            physicsWorld.destroyBody(playerBody);
            playerBody = null;
        }
    }
}
