package net.lapidist.colony.client.renderers;


import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;

/** Default implementation returning built-in asset references. */
public final class DefaultAssetResolver implements AssetResolver {

    @Override
    public String tileAsset(final String type) {
        TileDefinition def = Registries.tiles().get(type);
        if (def == null) {
            def = Registries.tiles().get("empty");
        }
        if (def == null || def.asset() == null) {
            return "dirt0";
        }
        return def.asset();
    }

    @Override
    public boolean hasTileAsset(final String type) {
        TileDefinition def = Registries.tiles().get(type);
        return def != null && def.asset() != null;
    }

    @Override
    public String buildingAsset(final String type) {
        BuildingDefinition def = Registries.buildings().get(type);
        if (def == null) {
            return "house0";
        }
        return def.asset() != null ? def.asset() : "house0";
    }

    @Override
    public boolean hasBuildingAsset(final String type) {
        BuildingDefinition def = Registries.buildings().get(type);
        return def != null && def.asset() != null;
    }
}
