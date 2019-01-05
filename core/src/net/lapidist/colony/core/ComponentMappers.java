package net.lapidist.colony.core;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.core.components.SpriteComponent;
import net.lapidist.colony.core.components.TileComponent;

public class ComponentMappers {

    public static ComponentMapper<SpriteComponent> sprites = ComponentMapper.getFor(SpriteComponent.class);
    public static ComponentMapper<TileComponent> tiles = ComponentMapper.getFor(TileComponent.class);
}
