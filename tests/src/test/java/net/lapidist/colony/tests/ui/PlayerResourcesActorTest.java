package net.lapidist.colony.tests.ui;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.client.ui.PlayerResourcesActor;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/** Tests for {@link PlayerResourcesActor}. */
@RunWith(GdxTestRunner.class)
public class PlayerResourcesActorTest {
    private static final int WOOD = 5;
    private static final int STONE = 4;
    private static final int FOOD = 3;

    @Test
    public void displaysCurrentResources() throws Exception {
        World world = new World(new WorldConfigurationBuilder().build());
        Entity player = world.createEntity();
        PlayerResourceComponent pr = new PlayerResourceComponent();
        pr.addWood(WOOD);
        pr.addStone(STONE);
        pr.addFood(FOOD);
        player.edit().add(pr);
        world.process();

        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        PlayerResourcesActor actor = new PlayerResourcesActor(skin, world);
        actor.act(0f);

        Field labelField = PlayerResourcesActor.class.getDeclaredField("label");
        labelField.setAccessible(true);
        Label label = (Label) labelField.get(actor);
        assertEquals("W:" + WOOD + " S:" + STONE + " F:" + FOOD, label.getText().toString());
    }
}
