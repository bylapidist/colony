package net.lapidist.colony.client.systems;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;

/**
 * Creates and updates Box2DLights for entities with {@link PointLightComponent}.
 */
public final class DynamicLightSystem extends BaseSystem implements Disposable {
    /** Factory for creating lights. */
    public interface LightFactory {
        PointLight create(RayHandler handler, PointLightComponent comp);
    }

    private static final int DEFAULT_RAYS = 16;

    private final LightingSystem lightingSystem;
    private final LightFactory factory;
    private ComponentMapper<PointLightComponent> lightMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private final IntMap<PointLight> lights = new IntMap<>();

    public DynamicLightSystem(final LightingSystem lighting) {
        this(lighting, (h, c) -> new PointLight(h, DEFAULT_RAYS, c.getColor(), c.getRadius(), 0f, 0f));
    }

    public DynamicLightSystem(final LightingSystem lighting, final LightFactory factoryParam) {
        this.lightingSystem = lighting;
        this.factory = factoryParam;
    }

    /** Number of active lights. */
    public int getLightCount() {
        return lights.size;
    }

    @Override
    public void initialize() {
        lightMapper = world.getMapper(PointLightComponent.class);
        playerMapper = world.getMapper(PlayerComponent.class);
    }

    @Override
    protected void processSystem() {
        RayHandler handler = lightingSystem.getRayHandler();
        if (handler == null) {
            return;
        }
        var subscription = world.getAspectSubscriptionManager()
                .get(Aspect.all(PointLightComponent.class));
        var ids = subscription.getEntities();
        // create or update
        int[] data = ids.getData();
        for (int i = 0, s = ids.size(); i < s; i++) {
            int id = data[i];
            PointLight light = lights.get(id);
            Entity e = world.getEntity(id);
            PointLightComponent comp = lightMapper.get(e);
            if (light == null) {
                light = factory.create(handler, comp);
                lights.put(id, light);
            }
            Color c = comp.getColor();
            light.setColor(c.r, c.g, c.b, comp.getIntensity());
            light.setDistance(comp.getRadius());
            PlayerComponent pc = playerMapper.get(e);
            if (pc != null) {
                light.setPosition(pc.getX(), pc.getY());
            }
        }
        // remove lights for missing entities
        IntMap.Keys keys = lights.keys();
        while (keys.hasNext) {
            int id = keys.next();
            if (!contains(ids, id)) {
                PointLight light = lights.remove(id);
                if (light != null) {
                    light.remove();
                }
            }
        }
    }

    private static boolean contains(final com.artemis.utils.IntBag bag, final int value) {
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
        IntMap.Values<PointLight> values = lights.values();
        while (values.hasNext()) {
            values.next().remove();
        }
        lights.clear();
    }
}
