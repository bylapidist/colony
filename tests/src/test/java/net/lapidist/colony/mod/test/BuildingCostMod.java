package net.lapidist.colony.mod.test;

import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;

/** Mod registering a custom building with a unique cost. */
public final class BuildingCostMod implements GameMod {
    private static final int HUT_WOOD = 3;
    @Override
    public void init() {
        Registries.buildings().register(new BuildingDefinition(
                "hut",
                "Hut",
                "house0",
                new ResourceData(HUT_WOOD, 0, 0),
                "Test hut"));
    }
}
