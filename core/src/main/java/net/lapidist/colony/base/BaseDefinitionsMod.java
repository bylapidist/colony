package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.util.I18n;

/** Built-in mod registering standard tile and building definitions. */
public final class BaseDefinitionsMod implements GameMod {
    private static final int HOUSE_WOOD = 1;
    private static final int MARKET_WOOD = 5;
    private static final int MARKET_STONE = 2;
    private static final int FACTORY_WOOD = 10;
    private static final int FACTORY_STONE = 5;
    private static final int FARM_WOOD = 2;
    @Override
    public void init() {
        Registries.tiles().register(new TileDefinition("empty", "Empty", "dirt0"));
        Registries.tiles().register(new TileDefinition("dirt", "Dirt", "dirt0"));
        Registries.tiles().register(new TileDefinition("grass", "Grass", "grass0"));


        Registries.buildings().register(new BuildingDefinition(
                "house",
                I18n.get("building.house"),
                "house0",
                new ResourceData(HOUSE_WOOD, 0, 0),
                I18n.get("building.house.desc")));
        Registries.buildings().register(new BuildingDefinition(
                "market",
                I18n.get("building.market"),
                "house0",
                new ResourceData(MARKET_WOOD, MARKET_STONE, 0),
                I18n.get("building.market.desc")));
        Registries.buildings().register(new BuildingDefinition(
                "factory",
                I18n.get("building.factory"),
                "house0",
                new ResourceData(FACTORY_WOOD, FACTORY_STONE, 0),
                I18n.get("building.factory.desc")));
        Registries.buildings().register(new BuildingDefinition(
                "farm",
                I18n.get("building.farm"),
                "house0",
                new ResourceData(FARM_WOOD, 0, 0),
                I18n.get("building.farm.desc")));
    }
}
