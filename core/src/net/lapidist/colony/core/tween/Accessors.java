package net.lapidist.colony.core.tween;

import aurelienribon.tweenengine.Tween;
import com.bitfire.postprocessing.effects.Bloom;
import net.lapidist.colony.core.core.Camera;

public class Accessors {

    public static void register() {
        Tween.registerAccessor(Bloom.class, new BloomEffectAccessor());
        Tween.registerAccessor(Camera.class, new CameraAccessor());
    }
}
