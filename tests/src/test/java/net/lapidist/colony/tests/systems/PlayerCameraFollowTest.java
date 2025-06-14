package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerInitSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PlayerCameraFollowTest {
    @Test
    public void cameraFollowsPlayerInPlayerMode() {
        KeyBindings keys = new KeyBindings();
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem(), new PlayerInitSystem(), new PlayerMovementSystem(keys))
                .build());
        world.process();
        world.setDelta(1f);
        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);

        int id = world.getAspectSubscriptionManager()
                .get(Aspect.all(PlayerComponent.class))
                .getEntities().get(0);
        PlayerComponent pc = world.getMapper(PlayerComponent.class).get(world.getEntity(id));
        final float deltaX = 10f;
        final float deltaY = 5f;
        pc.setX(pc.getX() + deltaX);
        pc.setY(pc.getY() + deltaY);

        world.process();
        world.setDelta(1f);
        world.process();
        world.setDelta(1f);
        world.process();

        final float epsilon = 0.001f;
        assertEquals(pc.getX(), camera.getCamera().position.x, epsilon);
        assertEquals(pc.getY(), camera.getCamera().position.y, epsilon);
    }
}
