package net.lapidist.colony.core.tween;

import aurelienribon.tweenengine.TweenAccessor;
import net.lapidist.colony.core.core.Camera;

public class CameraAccessor implements TweenAccessor<Camera> {

    public static final int POSITION_XY = 0;
    public static final int ZOOM = 1;

    @Override
    public int getValues(Camera target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY: {
                returnValues[0] = target.position.x;
                returnValues[1] = target.position.y;
                return 2;
            }

            case ZOOM: {
                returnValues[0] = target.zoom;
                return 1;
            }
        }

        return 0;
    }

    @Override
    public void setValues(Camera target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY: {
                target.position.x = newValues[0];
                target.position.y = newValues[1];
                break;
            }

            case ZOOM: {
                target.zoom = newValues[0];
                break;
            }
        }
    }
}
