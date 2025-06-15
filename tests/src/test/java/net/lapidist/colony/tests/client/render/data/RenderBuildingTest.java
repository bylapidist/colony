package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.render.RenderBuilding;
import org.junit.Test;

import static org.junit.Assert.*;

public class RenderBuildingTest {
    private static final int X = 4;
    private static final int Y = 7;

    @Test
    public void builderSetsFields() {
        RenderBuilding building = RenderBuilding.builder()
                .x(X)
                .y(Y)
                .buildingType("house")
                .build();

        assertEquals(X, building.getX());
        assertEquals(Y, building.getY());
        assertEquals("house", building.getBuildingType());
    }
}
