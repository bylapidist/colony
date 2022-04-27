package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;

public final class Mappers {

    private Mappers() {
    }

    public static final ComponentMapper<TextureRegionReferenceComponent> TEXTURE_REGIONS =
            ComponentMapper.getFor(TextureRegionReferenceComponent.class);
}
