package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.components.entities.TileComponent;

public final class Mappers {

    private Mappers() {
    }

    public static final ComponentMapper<TileComponent> TILES =
            ComponentMapper.getFor(TileComponent.class);
}
