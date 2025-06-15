package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import box2dLight.RayHandler;

/**
 * System that updates ambient lighting and clear color based on the time of day.
 */
public final class DayNightSystem extends BaseSystem {

    private final ClearScreenSystem clearScreenSystem;
    private final LightingSystem lightingSystem;
    private float timeOfDay;

    private static final float HOURS_PER_DAY = 24f;
    private static final float FULL_ROTATION = 360f;
    private static final float DAWN_OFFSET = 90f;

    public DayNightSystem(final ClearScreenSystem clearSystem,
                          final LightingSystem lighting) {
        this.clearScreenSystem = clearSystem;
        this.lightingSystem = lighting;
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
        timeOfDay = wrap(timeOfDay + world.getDelta());
        float brightness = calculateBrightness(timeOfDay);
        Color c = clearScreenSystem.getColor();
        c.set(brightness, brightness, brightness, 1f);
        RayHandler handler = lightingSystem.getRayHandler();
        if (handler != null) {
            handler.setAmbientLight(brightness, brightness, brightness, 1f);
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
}
