package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import box2dLight.RayHandler;
import net.lapidist.colony.components.state.MutableEnvironmentState;

/**
 * System that updates ambient lighting and clear color based on the time of day.
 */
public final class DayNightSystem extends BaseSystem {

    private final ClearScreenSystem clearScreenSystem;
    private final LightingSystem lightingSystem;
    private final MutableEnvironmentState environment;
    private float timeOfDay;

    private static final float HOURS_PER_DAY = 24f;
    private static final float FULL_ROTATION = 360f;
    private static final float DAWN_OFFSET = 90f;
    private static final float SUNRISE_TIME = 6f;
    private static final float NOON_TIME = 12f;
    private static final float SUNSET_TIME = 18f;
    private static final Color NIGHT_COLOR = new Color(0f, 0f, 0f, 1f);
    private static final Color SUNRISE_COLOR = new Color(0.6f, 0.5f, 0.4f, 1f);
    private static final Color DAY_COLOR = new Color(1f, 1f, 1f, 1f);
    private static final Color MOON_COLOR = new Color(0.2f, 0.2f, 0.25f, 1f);

    public DayNightSystem(final ClearScreenSystem clearSystem,
                          final LightingSystem lighting,
                          final MutableEnvironmentState env) {
        this.clearScreenSystem = clearSystem;
        this.lightingSystem = lighting;
        this.environment = env;
    }

    /** Current time of day between 0 and 24. */
    public float getTimeOfDay() {
        return timeOfDay;
    }

    /** Set the time of day, values wrap to the 0-24 range. */
    public void setTimeOfDay(final float time) {
        timeOfDay = wrap(time);
    }

    /**
     * Calculate the direction of the sun for the current time of day.
     *
     * @param out vector to store the result
     * @return normalized sun direction
     */
    public Vector3 getSunDirection(final Vector3 out) {
        float angle = (timeOfDay / HOURS_PER_DAY) * FULL_ROTATION - DAWN_OFFSET;
        return out.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle), 1f).nor();
    }

    @Override
    protected void processSystem() {
        timeOfDay = wrap(timeOfDay + world.getDelta());
        Color c = clearScreenSystem.getColor();
        calculateColor(timeOfDay, environment.getMoonPhase(), c);
        RayHandler handler = lightingSystem.getRayHandler();
        if (handler != null) {
            handler.setAmbientLight(c.r, c.g, c.b, 1f);
        }
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

    private static void calculateColor(final float time, final float moonPhase, final Color out) {
        gradientColor(time, out);
        float brightness = calculateBrightness(time);
        out.r *= brightness;
        out.g *= brightness;
        out.b *= brightness;
        float nightFactor = 1f - brightness;
        out.r = Math.min(1f, out.r + MOON_COLOR.r * moonPhase * nightFactor);
        out.g = Math.min(1f, out.g + MOON_COLOR.g * moonPhase * nightFactor);
        out.b = Math.min(1f, out.b + MOON_COLOR.b * moonPhase * nightFactor);
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
