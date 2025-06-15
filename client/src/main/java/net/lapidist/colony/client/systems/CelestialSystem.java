package net.lapidist.colony.client.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.entities.CelestialBodyComponent;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.MapState;

/** Updates celestial body positions based on the current environment. */
public final class CelestialSystem extends BaseSystem {
    private final net.lapidist.colony.client.network.GameClient client;
    private final java.util.function.Supplier<EnvironmentState> environment;
    private ComponentMapper<CelestialBodyComponent> bodyMapper;

    private static final float HOURS_PER_DAY = 24f;
    private static final float FULL_ROTATION = 360f;
    private static final float DAWN_OFFSET = 90f;

    public CelestialSystem(final net.lapidist.colony.client.network.GameClient clientToUse,
                           final java.util.function.Supplier<EnvironmentState> env) {
        this.client = clientToUse;
        this.environment = env;
    }

    @Override
    public void initialize() {
        bodyMapper = world.getMapper(CelestialBodyComponent.class);
    }

    @Override
    protected void processSystem() {
        int width = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        int height = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;
        float centerX = width * GameConstants.TILE_SIZE / 2f;
        float centerY = height * GameConstants.TILE_SIZE / 2f;
        var entities = world.getAspectSubscriptionManager()
                .get(Aspect.all(CelestialBodyComponent.class))
                .getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = world.getEntity(entities.get(i));
            CelestialBodyComponent body = bodyMapper.get(e);
            float radius = body.getOrbitRadius() > 0
                    ? body.getOrbitRadius()
                    : Math.max(width, height) * GameConstants.TILE_SIZE;
            EnvironmentState env = environment.get();
            float angle = (env.timeOfDay() / HOURS_PER_DAY) * FULL_ROTATION - DAWN_OFFSET
                    + body.getOrbitOffset();
            body.setX(centerX + MathUtils.cosDeg(angle) * radius);
            body.setY(centerY + MathUtils.sinDeg(angle) * radius);
        }
    }
}
