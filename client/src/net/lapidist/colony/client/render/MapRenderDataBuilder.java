package net.lapidist.colony.client.render;

import net.lapidist.colony.components.maps.MapComponent;

/** Utility to convert {@link MapComponent} to {@link MapRenderData}. */
public final class MapRenderDataBuilder {
    private MapRenderDataBuilder() {
    }

    public static MapRenderData fromMap(final MapComponent map) {
        return new SimpleMapRenderData(map.getTiles(), map.getEntities());
    }
}
