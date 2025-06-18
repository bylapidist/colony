package net.lapidist.colony.client.systems;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.client.events.ResizeEvent;
import net.mostlyoriginal.api.event.common.Subscribe;

/**
 * Consolidated lighting system combining dynamic lights and day/night cycle.
 */
public final class LightingSystem extends BaseSystem implements Disposable {
    /** Factory for creating lights. */
    public interface LightFactory {
        PointLight create(RayHandler handler, PointLightComponent comp);
    }

    public static final int DEFAULT_RAYS = 16;

    private final ClearScreenSystem clearScreenSystem;
    private final int rays;
    private final LightFactory factory;
    private final IntMap<PointLight> lights = new IntMap<>();

    private RayHandler rayHandler;
    private float timeOfDay;

    private ComponentMapper<PointLightComponent> lightMapper;
    private ComponentMapper<PlayerComponent> playerMapper;

    private static final float HOURS_PER_DAY = 24f;
    private static final float SUNRISE_TIME = 6f;
    private static final float NOON_TIME = 12f;
    private static final float SUNSET_TIME = 18f;
    private static final Color NIGHT_COLOR = new Color(0f, 0f, 0f, 1f);
    private static final Color SUNRISE_COLOR = new Color(0.6f, 0.5f, 0.4f, 1f);
    private static final Color DAY_COLOR = new Color(0.6f, 0.7f, 1f, 1f);
    private static final float NIGHT_AMBIENT_SCALE = 0.1f;

    public LightingSystem(final ClearScreenSystem clearSystem) {
        this(clearSystem, DEFAULT_RAYS);
    }

    public LightingSystem(final ClearScreenSystem clearSystem, final int rayCount) {
        this(clearSystem, (h, c) -> new PointLight(h, rayCount, c.getColor(), c.getRadius(), 0f, 0f), rayCount);
    }

    public LightingSystem(final ClearScreenSystem clearSystem, final LightFactory factoryParam) {
        this(clearSystem, factoryParam, DEFAULT_RAYS);
    }

    private LightingSystem(final ClearScreenSystem clearSystem, final LightFactory factoryParam, final int rayCount) {
        this.clearScreenSystem = clearSystem;
        this.factory = factoryParam;
        this.rays = rayCount;
    }

    /** Assign the handler used for lighting. */
    public void setRayHandler(final RayHandler handler) {
        this.rayHandler = handler;
        if (handler != null) {
            handler.useCustomViewport(
                    0,
                    0,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()
            );
        }
    }

    /** Current lighting handler or {@code null}. */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    @Subscribe
    private void onResize(final ResizeEvent event) {
        if (rayHandler != null) {
            rayHandler.useCustomViewport(0, 0, event.width(), event.height());
        }
    }

    /** Current time of day between 0 and 24. */
    public float getTimeOfDay() {
        return timeOfDay;
    }

    /** Set the time of day, values wrap to the 0-24 range. */
    public void setTimeOfDay(final float time) {
        timeOfDay = wrap(time);
    }

    /** Number of rays used when creating each light. */
    public int getRayCount() {
        return rays;
    }

    /** Number of active lights. */
    public int getLightCount() {
        return lights.size;
    }

    /** Calculate the direction of the sun for the current time of day. */
    public Vector3 getSunDirection(final Vector3 out) {
        float angle = (timeOfDay / HOURS_PER_DAY) * MathUtils.PI2 - MathUtils.HALF_PI;
        return out.set(0f, MathUtils.cos(angle), MathUtils.sin(angle)).nor();
    }

    @Override
    public void initialize() {
        lightMapper = world.getMapper(PointLightComponent.class);
        playerMapper = world.getMapper(PlayerComponent.class);
    }

    @Override
    protected void processSystem() {
        if (rayHandler != null) {
            rayHandler.update();
        }
        updateDayNight();
        updateDynamicLights();
    }

    private void updateDayNight() {
        timeOfDay = wrap(timeOfDay + world.getDelta());
        Color c = clearScreenSystem.getColor();
        gradientColor(timeOfDay, c);
        float brightness = 1f - calculateBrightness(timeOfDay);
        float ambient = brightness * NIGHT_AMBIENT_SCALE;
        if (rayHandler != null) {
            rayHandler.setAmbientLight(ambient, ambient, ambient, 1f);
        }
    }

    private void updateDynamicLights() {
        if (rayHandler == null) {
            return;
        }
        var subscription = world.getAspectSubscriptionManager()
                .get(Aspect.all(PointLightComponent.class));
        var ids = subscription.getEntities();
        int[] data = ids.getData();
        for (int i = 0, s = ids.size(); i < s; i++) {
            int id = data[i];
            PointLight light = lights.get(id);
            Entity e = world.getEntity(id);
            PointLightComponent comp = lightMapper.get(e);
            if (light == null) {
                light = factory.create(rayHandler, comp);
                lights.put(id, light);
            }
            Color color = comp.getColor();
            light.setColor(color.r, color.g, color.b, comp.getIntensity());
            light.setDistance(comp.getRadius());
            PlayerComponent pc = playerMapper.get(e);
            if (pc != null) {
                light.setPosition(pc.getX(), pc.getY());
            }
        }
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
        if (rayHandler != null) {
            rayHandler.dispose();
        }
        IntMap.Values<PointLight> values = lights.values();
        while (values.hasNext()) {
            values.next().remove();
        }
        lights.clear();
    }

    private static float wrap(final float time) {
        float t = time % HOURS_PER_DAY;
        if (t < 0f) {
            t += HOURS_PER_DAY;
        }
        return t;
    }

    private static float calculateBrightness(final float time) {
        float t = wrap(time);
        float dayProgress = MathUtils.clamp((t - SUNRISE_TIME) / (SUNSET_TIME - SUNRISE_TIME), 0f, 1f);
        return MathUtils.sin(dayProgress * MathUtils.PI);
    }

    private static void gradientColor(final float time, final Color out) {
        float t = wrap(time);
        if (t < SUNRISE_TIME) {
            lerp(NIGHT_COLOR, SUNRISE_COLOR, t / SUNRISE_TIME, out);
        } else if (t < NOON_TIME) {
            lerp(SUNRISE_COLOR, DAY_COLOR, (t - SUNRISE_TIME) / SUNRISE_TIME, out);
        } else if (t < SUNSET_TIME) {
            lerp(DAY_COLOR, SUNRISE_COLOR, (t - NOON_TIME) / SUNRISE_TIME, out);
        } else {
            lerp(SUNRISE_COLOR, NIGHT_COLOR, (t - SUNSET_TIME) / SUNRISE_TIME, out);
        }
    }

    private static void lerp(final Color from, final Color to, final float alpha, final Color out) {
        out.r = MathUtils.lerp(from.r, to.r, alpha);
        out.g = MathUtils.lerp(from.g, to.g, alpha);
        out.b = MathUtils.lerp(from.b, to.b, alpha);
        out.a = 1f;
    }
}
