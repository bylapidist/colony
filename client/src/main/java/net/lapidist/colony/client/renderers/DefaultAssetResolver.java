package net.lapidist.colony.client.renderers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;

/** Default implementation returning built-in asset references. */
public final class DefaultAssetResolver implements AssetResolver {

    private static final Map<String, String> TILE_ASSETS = new HashMap<>();
    private static final Map<String, String> BUILDING_ASSETS = new HashMap<>();

    static {
        TILE_ASSETS.put(TileComponent.TileType.EMPTY.name(), "dirt0");
        TILE_ASSETS.put(TileComponent.TileType.DIRT.name(), "dirt0");
        TILE_ASSETS.put(TileComponent.TileType.GRASS.name(), "grass0");

        BUILDING_ASSETS.put(BuildingComponent.BuildingType.HOUSE.name(), "house0");
        BUILDING_ASSETS.put(BuildingComponent.BuildingType.MARKET.name(), "house0");
        BUILDING_ASSETS.put(BuildingComponent.BuildingType.FACTORY.name(), "house0");
    }
    @Override
    public String tileAsset(final String type) {
        if (type == null) {
            return "dirt0";
        }
        String asset = TILE_ASSETS.get(type.toUpperCase(Locale.ROOT));
        return asset != null ? asset : "dirt0";
    }

    @Override
    public String buildingAsset(final String type) {
        if (type == null) {
            return "house0";
        }
        String asset = BUILDING_ASSETS.get(type.toUpperCase(Locale.ROOT));
        return asset != null ? asset : "house0";
    }
}
