package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Adds rendering components to entities based on their logical type.
 */
public final class RenderComponentFactory {

    private RenderComponentFactory() { }

    public static void addTileRendering(
            final World world,
            final Entity tile,
            final AssetResolver resolver
    ) {
        TileComponent component = world.getMapper(TileComponent.class).get(tile);
        String ref = resolver.tileAsset(component.getTileType());
        TextureRegionReferenceComponent texture = new TextureRegionReferenceComponent();
        texture.setResourceRef(ref);
        tile.edit().add(texture);
    }

    public static void addBuildingRendering(
            final World world,
            final Entity building,
            final AssetResolver resolver
    ) {
        BuildingComponent component = world.getMapper(BuildingComponent.class).get(building);
        String ref = resolver.buildingAsset(component.getBuildingType());
        TextureRegionReferenceComponent texture = new TextureRegionReferenceComponent();
        texture.setResourceRef(ref);
        building.edit().add(texture);
    }
}
