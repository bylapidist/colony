package net.lapidist.colony.tests.client.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.systems.ParticleSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/** Tests for {@link ParticleSystem}. */
@RunWith(GdxTestRunner.class)
public class ParticleSystemTest {

    @Test
    public void updatesAndRemovesCompletedEffects() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ParticleSystem system = new ParticleSystem(batch);
        ParticleEffect effect = mock(ParticleEffect.class);
        when(effect.isComplete()).thenReturn(false, true);

        system.spawn(effect);

        World world = new World(new WorldConfigurationBuilder().with(system).build());
        final float dt = 0.5f;
        world.setDelta(dt);
        world.process();
        verify(effect).update(dt);
        verify(effect).draw(batch);

        world.setDelta(dt);
        world.process();
        verify(effect, times(2)).isComplete();
        world.dispose();
    }
}
