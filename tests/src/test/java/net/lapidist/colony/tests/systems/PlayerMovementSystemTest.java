package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PlayerMovementSystemTest {
    @Test
    public void initializesWithPlayerEntity() throws Exception {
        KeyBindings keys = new KeyBindings();
        PlayerCameraSystem camera = new PlayerCameraSystem();
        PlayerMovementSystem movement = new PlayerMovementSystem(keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(camera, movement)
                .build());
        net.lapidist.colony.client.entities.PlayerFactory.create(
                world,
                null,
                new net.lapidist.colony.components.state.resources.ResourceData(),
                null
        );
        world.process();

        int count = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(net.lapidist.colony.components.entities.PlayerComponent.class))
                .getEntities().size();
        assertEquals(1, count);
    }
}
