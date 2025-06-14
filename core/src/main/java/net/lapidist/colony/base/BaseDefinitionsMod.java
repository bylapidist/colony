package net.lapidist.colony.base;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;

/** Built-in mod registering standard tile and building definitions. */
public final class BaseDefinitionsMod implements GameMod {
    @Override
    public void init() {
        Registries.tiles().register(new TileDefinition("empty", "Empty", "dirt0"));
        Registries.tiles().register(new TileDefinition("dirt", "Dirt", "dirt0"));
        Registries.tiles().register(new TileDefinition("grass", "Grass", "grass0"));

        Registries.buildings().register(new BuildingDefinition(
                "house",
                BuildingComponent.BuildingType.HOUSE.toString(),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "market",
                BuildingComponent.BuildingType.MARKET.toString(),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "factory",
                BuildingComponent.BuildingType.FACTORY.toString(),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "farm",
                BuildingComponent.BuildingType.FARM.toString(),
                "house0"));
    }
}
