package net.lapidist.colony;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.component.*;

public class ComponentMappers {

    public static ComponentMapper<DecalComponent> decal = ComponentMapper.getFor(DecalComponent.class);
    public static ComponentMapper<TileComponent> tiles = ComponentMapper.getFor(TileComponent.class);
    public static ComponentMapper<ResourceComponent> resources = ComponentMapper.getFor(ResourceComponent.class);
}
