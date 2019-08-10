package net.lapidist.colony.core.systems.player;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.Events;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractControlSystem;

@Wire
public class PlayerControlSystem extends AbstractControlSystem {

    private AbstractCameraSystem cameraSystem;

    private final float BASE_FRICTION = 1f;
    private final float BASE_ACCELERATION = BASE_FRICTION + 10f;
    private final float BASE_MAX_VELOCITY = 10f;
    private final float MIN_VELOCITY = 3f;
    private final float MIN_ZOOM = 1f;
    private final float MAX_ZOOM = 2f;
    private final float ZOOM_SPEED = 0.06f;
    private final Vector3 tmpPosition = new Vector3();
    private final Vector2 tmpVelocity = new Vector2();
    private final Vector2 lastGridTouch = new Vector2();

    public PlayerControlSystem() {
        super(Aspect.all());
    }

    @Override
    protected void initialize() {
        super.initialize();
        AbstractControlSystem.getInputMultiplexer().addProcessor(this);
    }

    private void processInput(int e) {
        tmpPosition.set(cameraSystem.camera.position);

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

        cameraSystem.camera.position.set(tmpPosition);
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

    public Vector2 getLastGridTouch() {
        return lastGridTouch;
    }

    @Override
    protected void process(int e) {
        processInput(e);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);

        tmpPosition.set(cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).x, cameraSystem.worldCoordsFromScreenCoords(screenX, screenY).y, 0);

        int estimatedGridX = (int) tmpPosition.x / Constants.PPM;
        int estimatedGridY = (int) tmpPosition.y / Constants.PPM;

        lastGridTouch.set(estimatedGridX, estimatedGridY);
        MessageManager.getInstance().dispatchMessage(0, null, Events.CLICK_TILE);
        return true;
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
