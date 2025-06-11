package net.lapidist.colony.client.render;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

/**
 * Basic immutable implementation of {@link MapRenderData}.
 */
public final class SimpleMapRenderData implements MapRenderData {
    private final Array<Entity> tiles;
    private final Array<Entity> entities;

    public SimpleMapRenderData(final Array<Entity> tilesToUse, final Array<Entity> entitiesToUse) {
        this.tiles = tilesToUse;
        this.entities = entitiesToUse;
    }

    @Override
    public Array<Entity> getTiles() {
        return tiles;
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }
}
