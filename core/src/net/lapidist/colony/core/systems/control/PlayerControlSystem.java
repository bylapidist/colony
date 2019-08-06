package net.lapidist.colony.core.systems.control;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.*;
import net.lapidist.colony.components.player.PlayerComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.map.ClickTileOutsideReachEvent;
import net.lapidist.colony.core.events.map.ClickTileWithinReachEvent;
import net.lapidist.colony.core.events.map.HoverTileOutsideReachEvent;
import net.lapidist.colony.core.events.map.HoverTileWithinReachEvent;
import net.lapidist.colony.core.systems.AbstractCameraSystem;
import net.lapidist.colony.core.systems.AbstractControlSystem;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends AbstractControlSystem {

    private AbstractCameraSystem cameraSystem;

    private final float BASE_FRICTION = 200f;
    private final float BASE_ACCELERATION = BASE_FRICTION + 300f;
    private final float BASE_MAX_VELOCITY = 100f;
    private final float MIN_VELOCITY = 3f;
    private final float MIN_ZOOM = 1f;
    private final float MAX_ZOOM = 2f;
    private final float ZOOM_SPEED = 0.06f;
    private final float BASE_REACH = 8.5f;
    private final Vector3 tmpPosition = new Vector3();
    private final Vector2 tmpOrigin = new Vector2();
    private final Vector2 tmpVelocity = new Vector2();
    private final Circle reachBounds = new Circle();
    private float lastRotation = 0f;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        getInputMultiplexer().addProcessor(this);
    }

    private void processInput(int e) {
        tmpVelocity.set(E(e).velocityComponentVelocity());
        tmpPosition.set(E(e).worldPositionComponentPosition());

        if (singleKeyDown(Input.Keys.W)) {
            accelerateVertically(-BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (singleKeyDown(Input.Keys.A)) {
            accelerateHorizontally(BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (singleKeyDown(Input.Keys.S)) {
            accelerateVertically(BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (singleKeyDown(Input.Keys.D)) {
            accelerateHorizontally(-BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            accelerateVertically(-BASE_ACCELERATION);
            accelerateHorizontally(-BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            accelerateHorizontally(BASE_ACCELERATION);
            accelerateVertically(-BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            accelerateVertically(BASE_ACCELERATION);
            accelerateHorizontally(-BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            accelerateHorizontally(BASE_ACCELERATION);
            accelerateVertically(BASE_ACCELERATION);
            lastRotation = MathUtils.atan2(tmpVelocity.y, tmpVelocity.x);
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

        tmpOrigin.set(
                tmpPosition.x + (E(e).textureComponentTexture().getWidth() / 2f),
                tmpPosition.y + (E(e).textureComponentTexture().getHeight() / 2f)
        );

        E(e).velocityComponentVelocity().set(tmpVelocity);
        E(e).worldPositionComponentPosition().set(tmpPosition);
        E(e).rotationComponentRotation(lastRotation);
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

    private void applyVerticalFriction(float friction) {
        tmpVelocity.y -= friction * Gdx.graphics.getDeltaTime();
        tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
    }

    private void applyHorizontalFriction(float friction) {
        tmpVelocity.x -= friction * Gdx.graphics.getDeltaTime();
        tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
    }

    private void accelerateHorizontally(float acceleration) {
        tmpVelocity.x += acceleration * Gdx.graphics.getDeltaTime();
        tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
    }

    private void accelerateVertically(float acceleration) {
        tmpVelocity.y += acceleration * Gdx.graphics.getDeltaTime();
        tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
    }

    @Override
    protected void process(int e) {
        reachBounds.set(
                tmpOrigin.x,
                tmpOrigin.y,
                (BASE_REACH * Constants.PPM) / 2f
        );

        tileHovered(
                Gdx.input.getX(),
                Gdx.input.getY()
        );

        processInput(e);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);

        tmpPosition.set(cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).x, cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).y, 0);

        int estimatedGridX = (int) tmpPosition.x / Constants.PPM;
        int estimatedGridY = (int) tmpPosition.y / Constants.PPM;

        if (reachBounds.contains(tmpPosition.x, tmpPosition.y)) {
            Events.fire(new ClickTileWithinReachEvent(estimatedGridX, estimatedGridY, tmpPosition.x, tmpPosition.y));
            return true;
        }

        Events.fire(new ClickTileOutsideReachEvent(estimatedGridX, estimatedGridY));

        return false;
    }

    public boolean tileHovered(int screenX, int screenY) {
        tmpPosition.set(cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).x, cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).y, 0);

        int estimatedGridX = (int) tmpPosition.x / Constants.PPM;
        int estimatedGridY = (int) tmpPosition.y / Constants.PPM;

        if (reachBounds.contains(tmpPosition.x, tmpPosition.y)) {
            Events.fire(new HoverTileWithinReachEvent(estimatedGridX, estimatedGridY, tmpPosition.x, tmpPosition.y));
            return true;
        }

        Events.fire(new HoverTileOutsideReachEvent(estimatedGridX, estimatedGridY));

        return false;
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
        getInputMultiplexer().removeProcessor(this);
    }
}
