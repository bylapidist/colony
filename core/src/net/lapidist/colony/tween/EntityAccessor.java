package net.lapidist.colony.tween;

import aurelienribon.tweenengine.TweenAccessor;
import net.lapidist.colony.entity.Entity;

public class EntityAccessor implements TweenAccessor<Entity> {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    @Override
    public int getValues(Entity target, int tweenType, float[] returnValues) {
        switch(tweenType) {
            case X:
                returnValues[0] = target.getX();
                return 1;
            case Y:
                returnValues[0] = target.getY();
                return 1;
            case Z:
                returnValues[0] = target.getZ();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Entity target, int tweenType, float[] newValues) {
        switch(tweenType) {
            case X:
                target.setX(newValues[0]);
                break;
            case Y:
                target.setY(newValues[0]);
                break;
            case Z:
                target.setZ(newValues[0]);
                break;
            default:
                assert false;
        }
    }
}
