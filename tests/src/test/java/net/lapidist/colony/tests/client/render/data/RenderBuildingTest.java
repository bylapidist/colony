package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.render.data.RenderBuilding;
import org.junit.Test;

import static org.junit.Assert.*;

public class RenderBuildingTest {
    private static final int X = 4;
    private static final int Y = 7;
    private static final float WORLD_X = 12f;
    private static final float WORLD_Y = 14f;

    @Test
    public void builderSetsFields() {
        RenderBuilding building = RenderBuilding.builder()
                .x(X)
                .y(Y)
                .worldX(WORLD_X)
                .worldY(WORLD_Y)
                .buildingType("HOUSE")
                .build();

        assertEquals(X, building.getX());
        assertEquals(Y, building.getY());
        assertEquals("HOUSE", building.getBuildingType());
        assertEquals(WORLD_X, building.getWorldX(), 0f);
        assertEquals(WORLD_Y, building.getWorldY(), 0f);
    }
}
