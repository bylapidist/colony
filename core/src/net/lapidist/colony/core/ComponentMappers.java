package net.lapidist.colony.core;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.core.components.traits.SpriteTrait;
import net.lapidist.colony.core.components.TileComponent;

public class ComponentMappers {

    // Components
    public static ComponentMapper<TileComponent> tiles = ComponentMapper.getFor(TileComponent.class);

    // Traits
    public static ComponentMapper<SpriteTrait> sprites = ComponentMapper.getFor(SpriteTrait.class);
}
