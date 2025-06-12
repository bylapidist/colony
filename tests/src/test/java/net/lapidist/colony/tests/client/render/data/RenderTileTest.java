package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.render.data.RenderTile;
import org.junit.Test;

import static org.junit.Assert.*;

public class RenderTileTest {
    private static final int X = 1;
    private static final int Y = 2;
    private static final int WOOD = 5;
    private static final int STONE = 3;
    private static final int FOOD = 2;
    private static final float WORLD_X = 10f;
    private static final float WORLD_Y = 20f;

    @Test
    public void builderSetsAllFields() {
        RenderTile tile = RenderTile.builder()
                .x(X)
                .y(Y)
                .worldX(WORLD_X)
                .worldY(WORLD_Y)
                .tileType("GRASS")
                .selected(true)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();

        assertEquals(X, tile.getX());
        assertEquals(Y, tile.getY());
        assertEquals("GRASS", tile.getTileType());
        assertTrue(tile.isSelected());
        assertEquals(WOOD, tile.getWood());
        assertEquals(STONE, tile.getStone());
        assertEquals(FOOD, tile.getFood());
        assertEquals(WORLD_X, tile.getWorldX(), 0f);
        assertEquals(WORLD_Y, tile.getWorldY(), 0f);
    }
}
