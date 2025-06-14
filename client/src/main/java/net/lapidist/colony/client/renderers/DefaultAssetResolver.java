package net.lapidist.colony.client.renderers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Default implementation returning built-in asset references. */
public final class DefaultAssetResolver implements AssetResolver {

    private static final Map<String, String> TILE_ASSETS = new HashMap<>();
    private static final Map<String, String> BUILDING_ASSETS = new HashMap<>();

    static {
        TILE_ASSETS.put("EMPTY", "dirt0");
        TILE_ASSETS.put("DIRT", "dirt0");
        TILE_ASSETS.put("GRASS", "grass0");

        BUILDING_ASSETS.put("HOUSE", "house0");
        BUILDING_ASSETS.put("MARKET", "house0");
        BUILDING_ASSETS.put("FACTORY", "house0");
        BUILDING_ASSETS.put("FARM", "house0");
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
    public boolean hasTileAsset(final String type) {
        if (type == null) {
            return false;
        }
        return TILE_ASSETS.containsKey(type.toUpperCase(Locale.ROOT));
    }

    @Override
    public String buildingAsset(final String type) {
        if (type == null) {
            return "house0";
        }
        String asset = BUILDING_ASSETS.get(type.toUpperCase(Locale.ROOT));
        return asset != null ? asset : "house0";
    }

    @Override
    public boolean hasBuildingAsset(final String type) {
        if (type == null) {
            return false;
        }
        return BUILDING_ASSETS.containsKey(type.toUpperCase(Locale.ROOT));
    }
}
