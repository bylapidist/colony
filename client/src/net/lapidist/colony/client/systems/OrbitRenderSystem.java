package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import net.lapidist.colony.components.OrbitalRadiusComponent;


import static net.lapidist.colony.client.entities.Mappers.*;

public final class OrbitRenderSystem extends IteratingSystem {

    private static final float TIME_STEP = 100f;
    private static final int PLANET_RADIUS = 8;
    private static final int STAR_RADIUS = 32;

    private final SpriteBatch batch = new SpriteBatch();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final long startTime = TimeUtils.millis();

    public OrbitRenderSystem() {
        super(Family.all(OrbitalRadiusComponent.class).get(), 0);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final float timeElapsed = TimeUtils.timeSinceMillis(startTime) / TIME_STEP;

        OrbitalRadiusComponent orbitC = ORBITS.get(entity);
        orbitC.setOrbitalPosition(
                orbitC.getCalculatedOrbitalPosition(timeElapsed)
        );

        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(
                orbitC.getOrigin().x,
                orbitC.getOrigin().y,
                orbitC.getRadius()
        );
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
                orbitC.getOrbitalPosition().x,
                orbitC.getOrbitalPosition().y,
                PLANET_RADIUS
        );
        shapeRenderer.circle(
                orbitC.getOrigin().x,
                orbitC.getOrigin().y,
                STAR_RADIUS
        );
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();

        batch.end();
    }
}
