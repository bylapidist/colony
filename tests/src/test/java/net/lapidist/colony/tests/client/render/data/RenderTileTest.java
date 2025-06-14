package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.registry.Registries;
import org.junit.Test;

import static org.junit.Assert.*;

public class RenderTileTest {
    private static final int X = 1;
    private static final int Y = 2;
    private static final int WOOD = 5;
    private static final int STONE = 3;
    private static final int FOOD = 2;

    @Test
    public void builderSetsAllFields() {
        new BaseDefinitionsMod().init();
        String id = Registries.tiles().get("grass").id();
        RenderTile tile = RenderTile.builder()
                .x(X)
                .y(Y)
                .tileType(id)
                .selected(true)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();

        assertEquals(X, tile.getX());
        assertEquals(Y, tile.getY());
        assertEquals(id, tile.getTileType());
        assertTrue(tile.isSelected());
        assertEquals(WOOD, tile.getWood());
        assertEquals(STONE, tile.getStone());
        assertEquals(FOOD, tile.getFood());
    }
}
