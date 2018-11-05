package net.lapidist.colony.core;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.core.components.DecalComponent;
import net.lapidist.colony.core.components.ModelComponent;
import net.lapidist.colony.core.components.ResourceComponent;
import net.lapidist.colony.core.components.TileComponent;

public class ComponentMappers {

    public static ComponentMapper<DecalComponent> decals = ComponentMapper.getFor(DecalComponent.class);
    public static ComponentMapper<ModelComponent> models = ComponentMapper.getFor(ModelComponent.class);
    public static ComponentMapper<TileComponent> tiles = ComponentMapper.getFor(TileComponent.class);
    public static ComponentMapper<ResourceComponent> resources = ComponentMapper.getFor(ResourceComponent.class);
}
