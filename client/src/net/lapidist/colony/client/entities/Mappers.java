package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.ComponentMapper;
import net.lapidist.colony.components.*;

public final class Mappers {

    private Mappers() {
    }

    public static final ComponentMapper<PlanetComponent> PLANETS =
            ComponentMapper.getFor(PlanetComponent.class);
    public static final ComponentMapper<StarComponent> STARS =
            ComponentMapper.getFor(StarComponent.class);
    public static final ComponentMapper<RenderableComponent> RENDERABLES =
            ComponentMapper.getFor(RenderableComponent.class);
}
