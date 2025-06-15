package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.i18n.I18n;

/** Built-in mod registering standard tile and building definitions. */
public final class BaseDefinitionsMod implements GameMod {
    @Override
    public void init() {
        Registries.tiles().register(new TileDefinition("empty", "Empty", "dirt0"));
        Registries.tiles().register(new TileDefinition("dirt", "Dirt", "dirt0"));
        Registries.tiles().register(new TileDefinition("grass", "Grass", "grass0"));


        Registries.buildings().register(new BuildingDefinition(
                "house",
                I18n.get("building.house"),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "market",
                I18n.get("building.market"),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "factory",
                I18n.get("building.factory"),
                "house0"));
        Registries.buildings().register(new BuildingDefinition(
                "farm",
                I18n.get("building.farm"),
                "house0"));
    }
}
