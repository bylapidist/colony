package net.lapidist.colony.client.renderers;


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
    String tileAsset(String type);

    /**
     * Resolve the texture or model identifier for a building type.
     *
     * @param type building type
     * @return asset reference for rendering
     */
    String buildingAsset(String type);
}
