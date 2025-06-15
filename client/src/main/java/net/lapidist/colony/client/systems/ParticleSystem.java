package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Updates and draws particle effects each frame. */
public final class ParticleSystem extends BaseSystem implements Disposable {

    private final SpriteBatch spriteBatch;
    private final Array<ParticleEffect> effects = new Array<>();

    public ParticleSystem(final SpriteBatch batch) {
        this.spriteBatch = batch;
    }

    /** Queue an effect for playback. */
    public void spawn(final ParticleEffect effect) {
        effects.add(effect);
    }

    @Override
    protected void processSystem() {
        float delta = world.getDelta();
        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffect effect = effects.get(i);
            effect.update(delta);
            effect.draw(spriteBatch);
            if (effect.isComplete()) {
                effects.removeIndex(i);
            }
        }
    }

    @Override
    public void dispose() {
        for (ParticleEffect effect : effects) {
            effect.dispose();
        }
        spriteBatch.dispose();
    }
}
