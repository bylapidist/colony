package net.lapidist.colony.core.core;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.tween.CameraAccessor;

import static net.lapidist.colony.core.Constants.PPM;
import static net.lapidist.colony.core.Constants.tweenManager;

public class Camera extends OrthographicCamera {

    private final static float MIN_ZOOM = 1f;
    private final static float MAX_ZOOM = 3f;
    private final static float INITIAL_ZOOM = 2f;
    private final static float ZOOM_SPEED = 0.06f;
    private final static float PAN_SPEED = PPM * 2f;
    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 mouse = new Vector2();
    private CameraState state;

    Camera(CameraState state) {
        super(PPM / 3f, (PPM / 3f) * (Graphics.width() / Graphics.height()));

        this.state = state;
        this.zoom = INITIAL_ZOOM;
        this.position.set(this.viewportWidth / 2f, this.viewportHeight / 2f, 0);
    }

    public static Vector2 screenCoords(float worldX, float worldY) {
        Core.camera.project(tmpVec3.set(worldX, worldY, 0));
        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    public static Vector2 worldCoords(float screenX, float screenY) {
        Core.camera.unproject(tmpVec3.set(screenX, screenY, 0));
        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    public CameraState getState() {
        return state;
    }

    public void setState(CameraState state) {
        this.state = state;
    }

    public void goToTile(TileComponent tile) {
        tweenToTile(tile, 0);
    }

    public void tweenToTile(TileComponent tile) {
        tweenToTile(tile, 0.6f);
    }

    public void tweenToTile(TileComponent tile, float duration) {
        Vector2 position = new Vector2(
                tile.tile.getBoundingBox().getX(),
                tile.tile.getBoundingBox().getY()
        );

        Tween.to(this, CameraAccessor.POSITION_XY, duration)
                .target(position.x, position.y)
                .start(tweenManager);
    }

    @Override
    public void update() {
        super.update();

        if (state == null) return;

        switch (state) {
            case PANNING_N: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x, position.y + PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case PANNING_NE: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x + PAN_SPEED, position.y + PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case PANNING_E: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x + PAN_SPEED, position.y)
                        .start(tweenManager);

                break;
            }

            case PANNING_SE: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x + PAN_SPEED, position.y - PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case PANNING_S: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x, position.y - PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case PANNING_SW: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x - PAN_SPEED, position.y - PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case PANNING_W: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x - PAN_SPEED, position.y)
                        .start(tweenManager);

                break;
            }

            case PANNING_NW: {
                Vector3 position = this.position.cpy();

                Tween.to(this, CameraAccessor.POSITION_XY, 0.1f)
                        .target(position.x - PAN_SPEED, position.y + PAN_SPEED)
                        .start(tweenManager);

                break;
            }

            case ZOOMING_IN: {
                this.zoom -= ZOOM_SPEED;
                this.zoom = MathUtils.clamp(this.zoom, MIN_ZOOM, MAX_ZOOM);

                setState(CameraState.STATIC);
                break;
            }

            case ZOOMING_OUT: {
                this.zoom += ZOOM_SPEED;
                this.zoom = MathUtils.clamp(this.zoom, MIN_ZOOM, MAX_ZOOM);

                setState(CameraState.STATIC);
                break;
            }

            case STATIC: {

            }

            default:
                // Do nothing
        }
    }

    public enum CameraState {
        STATIC,
        ZOOMING_IN,
        ZOOMING_OUT,
        PANNING_N,
        PANNING_NE,
        PANNING_E,
        PANNING_SE,
        PANNING_S,
        PANNING_SW,
        PANNING_W,
        PANNING_NW
    }
}
