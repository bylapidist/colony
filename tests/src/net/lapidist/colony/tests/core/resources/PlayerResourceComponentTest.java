package net.lapidist.colony.tests.core.resources;

import net.lapidist.colony.components.resources.PlayerResourceComponent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Unit tests for {@link PlayerResourceComponent}. */
public class PlayerResourceComponentTest {
    private static final int WOOD = 3;
    private static final int STONE = 2;
    private static final int FOOD = 1;

    @Test
    public void addMethodsIncreaseCounts() {
        PlayerResourceComponent pr = new PlayerResourceComponent();
        pr.addWood(WOOD);
        pr.addStone(STONE);
        pr.addFood(FOOD);
        assertEquals(WOOD, pr.getWood());
        assertEquals(STONE, pr.getStone());
        assertEquals(FOOD, pr.getFood());
    }
}
