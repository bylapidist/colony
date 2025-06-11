package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Maintains the {@link MapRenderData} used for rendering.
 */
public final class MapRenderDataSystem extends BaseSystem {
    private MapRenderData renderData;

    public MapRenderData getRenderData() {
        return renderData;
    }

    @Override
    public void initialize() {
        MapComponent map = MapUtils.findMap(world).orElse(null);
        if (map != null) {
            renderData = MapRenderDataBuilder.fromMap(map);
        }
    }

    @Override
    protected void processSystem() {
        // Future: update render data when map changes
    }
}
