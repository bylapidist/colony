package net.lapidist.colony.client.renderers;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;

/** Default implementation returning built-in asset references. */
public final class DefaultAssetResolver implements AssetResolver {
    @Override
    public String tileAsset(final TileComponent.TileType type) {
        return switch (type) {
            case EMPTY -> "dirt0";
            case GRASS -> "grass0";
        };
    }

    @Override
    public String buildingAsset(final BuildingComponent.BuildingType type) {
        return switch (type) {
            case HOUSE -> "house0";
            case MARKET -> "house0";
            case FACTORY -> "house0";
        };
    }
}
