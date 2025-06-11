package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.RenderComponentFactory;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Adds rendering components to map entities after the logical map has been created.
 * Runs once after {@link MapInitSystem}.
 */
public final class MapRenderInitSystem extends BaseSystem {

    @Override
    public void initialize() {
        MapComponent map = MapUtils.findMap(world).orElse(null);
        if (map == null) {
            return;
        }
        var resolver = new DefaultAssetResolver();
        for (int i = 0; i < map.getTiles().size; i++) {
            RenderComponentFactory.addTileRendering(world, map.getTiles().get(i), resolver);
        }
        for (int i = 0; i < map.getEntities().size; i++) {
            RenderComponentFactory.addBuildingRendering(world, map.getEntities().get(i), resolver);
        }
    }

    @Override
    protected void processSystem() {
        // initialization occurs once in initialize
    }
}
