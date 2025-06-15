package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import box2dLight.RayHandler;
import net.lapidist.colony.components.state.EnvironmentState;

/**
 * System that updates ambient lighting and clear color based on the time of day.
 */
public final class DayNightSystem extends BaseSystem {

    private final ClearScreenSystem clearScreenSystem;
    private final LightingSystem lightingSystem;
    private final java.util.function.Supplier<EnvironmentState> environment;
    private float timeOfDay;

    private static final float HOURS_PER_DAY = 24f;
    private static final float FULL_ROTATION = 360f;
    private static final float DAWN_OFFSET = 90f;
    private static final float SUNRISE_TIME = 6f;
    private static final float NOON_TIME = 12f;
    private static final float SUNSET_TIME = 18f;
    private static final Color NIGHT_COLOR = new Color(0.05f, 0.05f, 0.1f, 1f);
    private static final Color SUNRISE_COLOR = new Color(1f, 0.6f, 0.4f, 1f);
    private static final Color DAY_COLOR = new Color(1f, 1f, 1f, 1f);
    private static final Color MOON_COLOR = new Color(0.4f, 0.4f, 0.5f, 1f);

    public DayNightSystem(final ClearScreenSystem clearSystem,
                          final LightingSystem lighting,
                          final java.util.function.Supplier<EnvironmentState> env) {
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

    @Override
    protected void processSystem() {
        EnvironmentState env = environment.get();
        timeOfDay = env != null ? wrap(env.timeOfDay()) : wrap(timeOfDay + world.getDelta());
        Color c = clearScreenSystem.getColor();
        float moon = env != null ? env.moonPhase() : 0f;
        calculateColor(timeOfDay, moon, c);
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
        float angle = (time / HOURS_PER_DAY) * FULL_ROTATION - DAWN_OFFSET;
        return (MathUtils.sinDeg(angle) + 1f) / 2f;
    }

    private static void calculateColor(final float time, final float moonPhase, final Color out) {
        gradientColor(time, out);
        float brightness = calculateBrightness(time);
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
