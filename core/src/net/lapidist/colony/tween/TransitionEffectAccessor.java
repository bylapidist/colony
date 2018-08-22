package net.lapidist.colony.tween;

import aurelienribon.tweenengine.TweenAccessor;
import net.lapidist.colony.screen.TransitionEffect;

public class TransitionEffectAccessor implements TweenAccessor<TransitionEffect> {

    public static final int ALPHA = 0;

    @Override
    public int getValues(TransitionEffect target, int tweenType, float[] returnValues) {
        switch(tweenType) {
            case ALPHA:
                returnValues[0] = target.getOpacity();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(TransitionEffect target, int tweenType, float[] newValues) {
        switch(tweenType) {
            case ALPHA:
                target.setOpacity(newValues[0]);
                break;
            default:
                assert false;
        }
    }
}
