package net.lapidist.colony.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.bitfire.postprocessing.effects.Bloom;

public class BloomEffectAccessor implements TweenAccessor<Bloom> {

    public static final int INTENSITY = 0;

    @Override
    public int getValues(Bloom target, int tweenType, float[] returnValues) {
        switch(tweenType) {
            case INTENSITY:
                returnValues[0] = target.getBloomIntensity();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Bloom target, int tweenType, float[] newValues) {
        switch(tweenType) {
            case INTENSITY:
                target.setBloomIntesity(newValues[0]);;
                break;
            default:
                assert false;
        }
    }
}
