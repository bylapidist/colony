package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.render.MapRenderData;
import net.lapidist.colony.render.MapRenderDataBuilder;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Maintains the {@link MapRenderData} used for rendering.
 */
public final class MapRenderDataSystem extends BaseSystem {
    private MapRenderData renderData;
    private MapComponent map;
    private int lastVersion;

    public MapRenderData getRenderData() {
        return renderData;
    }

    @Override
    public void initialize() {
        map = MapUtils.findMap(world).orElse(null);
        if (map != null) {
            renderData = MapRenderDataBuilder.fromMap(map, world);
            lastVersion = map.getVersion();
        }
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
            if (map == null) {
                return;
            }
        }
        if (map.getVersion() != lastVersion) {
            renderData = MapRenderDataBuilder.fromMap(map, world);
            lastVersion = map.getVersion();
        }
    }
}
