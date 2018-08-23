package net.lapidist.colony.core;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.component.TileComponent;
import net.lapidist.colony.tween.CameraAccessor;

import static net.lapidist.colony.Constants.*;

public class Camera extends PerspectiveCamera {

    private final static float MIN_ZOOM = 800f;
    private final static float MAX_ZOOM = MIN_ZOOM * 4;
    private final static float ZOOM_SPEED = 300f;
    private final static float PAN_SPEED = PPM * 2;
    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 mouse = new Vector2();

    public static Vector2 screenCoords(float worldX, float worldY) {
        Core.camera.project(tmpVec3.set(worldX, worldY, 0));
        return mouse.set(tmpVec3.x, tmpVec3.y);
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

    private CameraState state;

    public Camera(CameraState state) {
        super(67, Graphics.width(), Graphics.height());

        this.position.set(0f, -400f, MIN_ZOOM * 2);
        this.lookAt(0, 0, 0);
        this.state = state;
        this.near = 1f;
        this.far = 8000f;
    }

    public void setState(CameraState state) {
        this.state = state;
    }

    public CameraState getState() {
        return state;
    }

    public void goToTile(TileComponent tile) {
        tweenToTile(tile, 0);
    }

    public void tweenToTile(TileComponent tile) {
        tweenToTile(tile, 0.6f);
    }

    public void tweenToTile(TileComponent tile, float duration) {
        Vector2 position = new Vector2(
            tile.hex.getExternalBoundingBox().getX(),
            tile.hex.getExternalBoundingBox().getY() - PPM * 3f
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
                float targetZoom = this.position.z - ZOOM_SPEED;

                if (targetZoom <= MIN_ZOOM) {
                    this.position.z = MIN_ZOOM;
                } else {
                    Tween.to(this, CameraAccessor.ZOOM, 0.1f)
                            .target(this.position.z - ZOOM_SPEED)
                            .start(tweenManager);
                }

                setState(CameraState.STATIC);
                break;
            }

            case ZOOMING_OUT: {
                float targetZoom = this.position.z + ZOOM_SPEED;

                if (targetZoom >= MAX_ZOOM) {
                    this.position.z = MAX_ZOOM;
                } else {
                    Tween.to(this, CameraAccessor.ZOOM, 0.1f)
                            .target(this.position.z + ZOOM_SPEED)
                            .start(tweenManager);
                }

                setState(CameraState.STATIC);
                break;
            }

            case STATIC: {

            }

            default:
                // Do nothing
        }
    }
}
