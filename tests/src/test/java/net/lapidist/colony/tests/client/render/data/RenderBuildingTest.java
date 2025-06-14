package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.registry.Registries;
import org.junit.Test;

import static org.junit.Assert.*;

public class RenderBuildingTest {
    private static final int X = 4;
    private static final int Y = 7;

    @Test
    public void builderSetsFields() {
        new BaseDefinitionsMod().init();
        String id = Registries.buildings().get("house").id();
        RenderBuilding building = RenderBuilding.builder()
                .x(X)
                .y(Y)
                .buildingType(id)
                .build();

        assertEquals(X, building.getX());
        assertEquals(Y, building.getY());
        assertEquals(id, building.getBuildingType());
    }
}
