package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.ResourceDefinition;
import net.lapidist.colony.registry.Registries;

/** Built-in mod registering default resource definitions. */
public final class BaseResourcesMod implements GameMod {
    @Override
    public void init() {
        Registries.resources().register(new ResourceDefinition("WOOD", "Wood", "wood0"));
        Registries.resources().register(new ResourceDefinition("STONE", "Stone", "stone0"));
        Registries.resources().register(new ResourceDefinition("FOOD", "Food", "food0"));
    }
}
