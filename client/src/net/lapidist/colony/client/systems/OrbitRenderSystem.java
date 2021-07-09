package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.OrbitalRadiusComponent;

import static net.lapidist.colony.client.entities.Mappers.*;

public class OrbitRenderSystem extends IteratingSystem {

    final SpriteBatch batch = new SpriteBatch();
    final OrthographicCamera camera = new OrthographicCamera();
    final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public OrbitRenderSystem() {
        super(Family.all(OrbitalRadiusComponent.class).get(), 0);
    }

    private Vector2 getOrbitalPosition(
            final Vector2 origin,
            final Vector2 radius,
            final float angle
    ) {
        return new Vector2(
                origin.x + MathUtils.cos(angle) * radius.x,
                origin.y + MathUtils.sin(angle) * radius.y
        );
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        OrbitalRadiusComponent orbitalRadiusComponent = ORBITS.get(entity);

        orbitalRadiusComponent.setOrbitalPosition(
                getOrbitalPosition(
                        orbitalRadiusComponent.getOrigin(),
                        orbitalRadiusComponent.getRadius(),
                        orbitalRadiusComponent.getAngle()
                )
        );

        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
                orbitalRadiusComponent.getOrbitalPosition().x,
                orbitalRadiusComponent.getOrbitalPosition().y,
                8
        );
        shapeRenderer.circle(
                orbitalRadiusComponent.getOrigin().x,
                orbitalRadiusComponent.getOrigin().y,
                32
        );
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();

        batch.end();
    }
}
