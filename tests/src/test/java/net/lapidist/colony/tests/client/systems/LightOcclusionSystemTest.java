package net.lapidist.colony.tests.client.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.systems.LightOcclusionSystem;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/** Tests for {@link LightOcclusionSystem}. */
@RunWith(GdxTestRunner.class)
@SuppressWarnings("checkstyle:magicnumber")
public class LightOcclusionSystemTest {

    private static final float POS_X = 3f;
    private static final float POS_Y = 4f;
    private static final float EPSILON = 0.001f;

    @Test
    public void createsAndRemovesBodies() {
        com.badlogic.gdx.physics.box2d.World box =
                new com.badlogic.gdx.physics.box2d.World(new Vector2(), false);
        LightOcclusionSystem system = new LightOcclusionSystem(box);
        World world = new World(new WorldConfigurationBuilder().with(system).build());

        var building = world.createEntity();
        BuildingComponent bc = new BuildingComponent();
        bc.setX(0);
        bc.setY(0);
        building.edit().add(bc);

        var player = world.createEntity();
        PlayerComponent pc = new PlayerComponent();
        pc.setX(1f);
        pc.setY(2f);
        player.edit().add(pc);

        world.process();
        assertEquals(2, box.getBodyCount());

        building.deleteFromWorld();
        world.process();
        assertEquals(1, box.getBodyCount());
        world.dispose();
        box.dispose();
    }

    @Test
    public void updatesPlayerBodyPosition() {
        com.badlogic.gdx.physics.box2d.World box =
                new com.badlogic.gdx.physics.box2d.World(new Vector2(), false);
        LightOcclusionSystem system = new LightOcclusionSystem(box);
        World world = new World(new WorldConfigurationBuilder().with(system).build());

        var player = world.createEntity();
        PlayerComponent pc = new PlayerComponent();
        pc.setX(POS_X);
        pc.setY(POS_Y);
        player.edit().add(pc);

        world.process();
        com.badlogic.gdx.utils.Array<com.badlogic.gdx.physics.box2d.Body> bodies =
                new com.badlogic.gdx.utils.Array<>();
        box.getBodies(bodies);
        com.badlogic.gdx.physics.box2d.Body body = bodies.get(0);
        assertEquals(POS_X, body.getPosition().x, EPSILON);
        assertEquals(POS_Y, body.getPosition().y, EPSILON);
        world.dispose();
        box.dispose();
    }
}
