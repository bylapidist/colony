package net.lapidist.colony.tests.graphics;

import net.lapidist.colony.client.graphics.BloomPostProcessor;
import net.lapidist.colony.tests.GdxTestRunner;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class BloomPostProcessorTest {

    @Test
    public void initializesFrameBuffers() {
        BloomPostProcessor pp = new BloomPostProcessor(mock(SpriteBatch.class));
        pp.dispose();
        assertNotNull(pp);
    }
}
