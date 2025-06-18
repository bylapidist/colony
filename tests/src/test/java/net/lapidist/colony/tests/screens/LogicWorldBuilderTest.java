package net.lapidist.colony.tests.screens;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.screens.LogicWorldBuilder;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class LogicWorldBuilderTest {

    private static final class DummySystem extends BaseSystem {
        private boolean processed = false;

        boolean isProcessed() {
            return processed;
        }

        @Override
        protected void processSystem() {
            processed = true;
        }
    }

    @Test
    public void allowsCustomSystemInjection() {
        DummySystem dummy = new DummySystem();
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(new EventSystem(), dummy);
        World world = LogicWorldBuilder.build(
                builder,
                null,
                null,
                new net.lapidist.colony.components.state.ResourceData(),
                null
        );
        world.setDelta(0f);
        world.process();
        assertTrue(world.getSystem(DummySystem.class).isProcessed());
        world.dispose();
    }
}
