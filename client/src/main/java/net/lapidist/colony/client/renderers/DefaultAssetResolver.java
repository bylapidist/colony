package net.lapidist.colony.client.renderers;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;

/** Default implementation returning built-in asset references. */
public final class DefaultAssetResolver implements AssetResolver {
    @Override
    public String tileAsset(final String type) {
        if (type == null) {
            return "dirt0";
        }
        try {
            TileComponent.TileType tile = TileComponent.TileType
                    .valueOf(type.toUpperCase(java.util.Locale.ROOT));
            return switch (tile) {
                case EMPTY, DIRT -> "dirt0";
                case GRASS -> "grass0";
            };
        } catch (IllegalArgumentException ex) {
            return "dirt0";
        }
    }

    @Override
    public String buildingAsset(final String type) {
        if (type == null) {
            return "house0";
        }
        try {
            BuildingComponent.BuildingType building = BuildingComponent.BuildingType
                    .valueOf(type.toUpperCase(java.util.Locale.ROOT));
            return switch (building) {
                case HOUSE -> "house0";
                case MARKET -> "house0";
                case FACTORY -> "house0";
            };
        } catch (IllegalArgumentException ex) {
            return "house0";
        }
    }
}
