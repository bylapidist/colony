package net.lapidist.colony.client.systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public class InputSystem extends IteratingSystem implements InputProcessor {

    private static final float BASE_FRICTION = 1f;

    private static final float BASE_ACCELERATION = 10f;

    private static final float BASE_MAX_VELOCITY = 10f;

    private static final float MIN_VELOCITY = 3f;

    private static final float MIN_ZOOM = 1f;

    private static final float MAX_ZOOM = 2f;

    private static final float ZOOM_SPEED = 0.06f;

    private final Vector3 tmpPosition = new Vector3();

    private final Vector2 tmpVelocity = new Vector2();

    private final IntSet downKeys =
            new IntSet(20);

    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private PlayerCameraSystem cameraSystem;

    public InputSystem() {
        super(Aspect.all());
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void process(final int entityId) {
        tmpPosition.set(cameraSystem.getCamera().position);

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
        limitToMapBounds();

        cameraSystem.getCamera().position.set(tmpPosition);
    }

    @Override
    protected void initialize() {
        Events.getInstance().addListener(event -> {
            ResizePayload payload = (ResizePayload) event.extraInfo;
            cameraSystem.getViewport().update(
                    payload.getWidth(),
                    payload.getHeight(),
                    false
            );
            return false;
        }, EventType.RESIZE.getOrdinal());
    }

    @Override
    protected void dispose() {
        inputMultiplexer.removeProcessor(this);
    }

    public final boolean singleKeyDown(final int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    public final boolean twoKeysDown(
            final int keycode1,
            final int keycode2
    ) {
        return downKeys.contains(keycode1)
                && downKeys.contains(keycode2)
                && downKeys.size == 2;
    }

    public final boolean multipleKeysDown(final int lastKeycode) {
        return false;
    }

    private void limitToMapBounds() {
        int mapPixelGutter = Constants.TILE_SIZE * Constants.MAP_GUTTER;
        int mapPixelWidth = Constants.MAP_WIDTH * Constants.TILE_SIZE;
        int mapPixelHeight = Constants.MAP_HEIGHT * Constants.TILE_SIZE;

        if (tmpPosition.x <= -mapPixelGutter) {
            tmpPosition.x = -mapPixelGutter;
            tmpVelocity.x = 0;
        }

        if (tmpPosition.x >= mapPixelWidth + mapPixelGutter) {
            tmpPosition.x = mapPixelWidth + mapPixelGutter;
            tmpVelocity.x = 0;
        }

        if (tmpPosition.y <= -mapPixelGutter) {
            tmpPosition.y = -mapPixelGutter;
            tmpVelocity.y = 0;
        }

        if (tmpPosition.y >= mapPixelHeight + mapPixelGutter) {
            tmpPosition.y = mapPixelHeight + mapPixelGutter;
            tmpVelocity.y = 0;
        }
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

    private void applyVerticalFriction(final float friction) {
        tmpVelocity.y -= friction;
        tmpPosition.y -= tmpVelocity.y;
    }

    private void applyHorizontalFriction(final float friction) {
        tmpVelocity.x -= friction;
        tmpPosition.x -= tmpVelocity.x;
    }

    private void accelerateHorizontally(final float acceleration) {
        tmpVelocity.x += acceleration;
        tmpPosition.x -= tmpVelocity.x;
    }

    private void accelerateVertically(final float acceleration) {
        tmpVelocity.y += acceleration;
        tmpPosition.y -= tmpVelocity.y;
    }

    @Override
    public final boolean keyDown(final int keycode) {
        downKeys.add(keycode);

        if (downKeys.size >= 2) {
            return multipleKeysDown(keycode);
        }

        return false;
    }

    @Override
    public final boolean keyUp(final int keycode) {
        downKeys.remove(keycode);
        return false;
    }

    @Override
    public final boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public final boolean touchDown(
            final int screenX,
            final int screenY,
            final int pointer,
            final int button
    ) {
        downKeys.add(button);
        return false;
    }

    @Override
    public final boolean touchUp(
            final int screenX,
            final int screenY,
            final int pointer,
            final int button
    ) {
        downKeys.remove(button);
        return false;
    }

    @Override
    public final boolean touchDragged(
            final int screenX,
            final int screenY,
            final int pointer
    ) {
        return false;
    }

    @Override
    public final boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public final boolean scrolled(final float amountX, final float amountY) {
        if (amountY >= 1) {
            cameraSystem.getCamera().zoom =
                    MathUtils.clamp(
                            cameraSystem.getCamera().zoom + ZOOM_SPEED,
                            MIN_ZOOM,
                            MAX_ZOOM
                    );
        }

        // Zoom in
        if (amountY <= -1) {
            cameraSystem.getCamera().zoom =
                    MathUtils.clamp(
                            cameraSystem.getCamera().zoom - ZOOM_SPEED,
                            MIN_ZOOM,
                            MAX_ZOOM
                    );
        }

        return false;
    }
}
