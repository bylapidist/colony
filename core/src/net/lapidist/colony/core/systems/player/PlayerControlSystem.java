package net.lapidist.colony.core.systems.player;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.player.PlayerComponent;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractControlSystem;
import net.lapidist.colony.core.systems.map.MapGenerationSystem;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends AbstractControlSystem {

    private Circle reachBounds;
    private AbstractCameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;

    private final static float BASE_FRICTION = 200f;
    private final static float BASE_ACCELERATION = BASE_FRICTION + 300f;
    private final static float BASE_MAX_VELOCITY = 100f;
    private final static float MIN_VELOCITY = 3f;
    private final static float MIN_ZOOM = 1f;
    private final static float MAX_ZOOM = 2f;
    private final static float ZOOM_SPEED = 0.06f;

    private final Vector2 position = new Vector2();
    private final Vector2 origin = new Vector2();
    private final Vector3 tmpPosition = new Vector3();
    private final Vector2 tmpVelocity = new Vector2();
    private final Vector2 tmpOrigin = new Vector2();

    public PlayerControlSystem() {
        super(Aspect.all(PlayerComponent.class));

        inputMultiplexer.addProcessor(this);
    }

    private void processInput(int e) {
        tmpVelocity.set(E(e).velocityComponentVelocity());
        tmpPosition.set(E(e).worldPositionComponentPosition());

        if (singleKeyDown(Input.Keys.W)) {
            accelerateVertically(-BASE_ACCELERATION);
        }

        if (singleKeyDown(Input.Keys.A)) {
            accelerateHorizontally(BASE_ACCELERATION);
        }

        if (singleKeyDown(Input.Keys.S)) {
            accelerateVertically(BASE_ACCELERATION);
        }

        if (singleKeyDown(Input.Keys.D)) {
            accelerateHorizontally(-BASE_ACCELERATION);
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            accelerateVertically(-BASE_ACCELERATION);
            accelerateHorizontally(-BASE_ACCELERATION);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            accelerateHorizontally(BASE_ACCELERATION);
            accelerateVertically(-BASE_ACCELERATION);
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            accelerateVertically(BASE_ACCELERATION);
            accelerateHorizontally(-BASE_ACCELERATION);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            accelerateHorizontally(BASE_ACCELERATION);
            accelerateVertically(BASE_ACCELERATION);
        }

        if (tmpVelocity.x > 0) {
            applyHorizontalFriction(BASE_FRICTION);
        }

        if (tmpVelocity.x < 0) {
            applyHorizontalFriction(-BASE_FRICTION);
        }

        if (tmpVelocity.y > 0) {
            applyVerticalFriction(BASE_FRICTION);
        }

        if (tmpVelocity.y < 0) {
            applyVerticalFriction(-BASE_FRICTION);
        }

        limitVelocity();

        E(e).velocityComponentVelocity().set(tmpVelocity);
        E(e).worldPositionComponentPosition().set(tmpPosition);
    }

    private void limitVelocity() {
        if (tmpVelocity.x < -BASE_MAX_VELOCITY) {
            tmpVelocity.x = -BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.y < -BASE_MAX_VELOCITY) {
            tmpVelocity.y = -BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.x > BASE_MAX_VELOCITY) {
            tmpVelocity.x = BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.y > BASE_MAX_VELOCITY) {
            tmpVelocity.y = BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.x < MIN_VELOCITY && tmpVelocity.x > -MIN_VELOCITY) {
            tmpVelocity.x = 0;
        }

        if (tmpVelocity.y < MIN_VELOCITY && tmpVelocity.y > -MIN_VELOCITY) {
            tmpVelocity.y = 0;
        }
    }

    private void applyVerticalFriction(float baseFriction) {
        tmpVelocity.y -= baseFriction * Gdx.graphics.getDeltaTime();
        tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
    }

    private void applyHorizontalFriction(float baseFriction) {
        tmpVelocity.x -= baseFriction * Gdx.graphics.getDeltaTime();
        tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
    }

    private void accelerateHorizontally(float baseAcceleration) {
        tmpVelocity.x += baseAcceleration * Gdx.graphics.getDeltaTime();
        tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
    }

    private void accelerateVertically(float v) {
        tmpVelocity.y += v * Gdx.graphics.getDeltaTime();
        tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
    }

    @Override
    protected void process(int e) {
        processInput(e);
    }

    @Override
    public boolean scrolled(int amount) {
        // Zoom out
        if (amount == 1) {
            cameraSystem.camera.zoom += ZOOM_SPEED;
            cameraSystem.camera.zoom = MathUtils.clamp(cameraSystem.camera.zoom, MIN_ZOOM, MAX_ZOOM);
        }

        // Zoom in
        if (amount == -1) {
            cameraSystem.camera.zoom -= ZOOM_SPEED;
            cameraSystem.camera.zoom = MathUtils.clamp(cameraSystem.camera.zoom, MIN_ZOOM, MAX_ZOOM);
        }

        return false;
    }

    @Override
    protected void dispose() {
        inputMultiplexer.removeProcessor(this);
    }
}
