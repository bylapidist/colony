package net.lapidist.colony.tween;

import aurelienribon.tweenengine.Tween;
import com.bitfire.postprocessing.effects.Bloom;
import net.lapidist.colony.entity.Entity;
import net.lapidist.colony.screen.TransitionEffect;

public class Accessors {

    public static void register() {
        Tween.registerAccessor(Entity.class, new EntityAccessor());
        Tween.registerAccessor(TransitionEffect.class, new TransitionEffectAccessor());
        Tween.registerAccessor(Bloom.class, new BloomEffectAccessor());
    }
}
