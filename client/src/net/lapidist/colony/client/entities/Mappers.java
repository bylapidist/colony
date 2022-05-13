package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.MapComponent;

public final class Mappers {

    private Mappers() {
    }

    public static final ComponentMapper<TextureRegionReferenceComponent> TEXTURE_REGIONS =
            ComponentMapper.getFor(TextureRegionReferenceComponent.class);

    public static final ComponentMapper<MapComponent> MAPS =
            ComponentMapper.getFor(MapComponent.class);
}
