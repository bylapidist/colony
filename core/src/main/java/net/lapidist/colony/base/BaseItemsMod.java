package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.ItemDefinition;
import net.lapidist.colony.registry.Registries;

/** Built-in mod registering default item definitions. */
public final class BaseItemsMod implements GameMod {
    @Override
    public void init() {
        Registries.items().register(new ItemDefinition("stick", "Stick", "stick0"));
        Registries.items().register(new ItemDefinition("stone", "Stone", "stone0"));
        Registries.items().register(new ItemDefinition("wood", "Wood", "wood0"));
        Registries.items().register(new ItemDefinition("food", "Food", "food0"));
    }
}
