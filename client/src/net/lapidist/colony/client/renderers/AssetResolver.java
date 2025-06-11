package net.lapidist.colony.client.renderers;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Resolves rendering resource references for map entities.
 */
public interface AssetResolver {
    /**
     * Resolve the texture or model identifier for a tile type.
     *
     * @param type tile type
     * @return asset reference for rendering
     */
    String tileAsset(TileComponent.TileType type);

    /**
     * Resolve the texture or model identifier for a building type.
     *
     * @param type building type
     * @return asset reference for rendering
     */
    String buildingAsset(BuildingComponent.BuildingType type);
}
