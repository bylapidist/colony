package net.lapidist.colony.components.maps;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class MapComponent implements Component {

    private Array<Entity> tiles;

    private Array<Entity> entities;

    public final Array<Entity> getTiles() {
        return tiles;
    }

    public final Array<Entity> getEntities() {
        return entities;
    }

    public final void setTiles(final Array<Entity> tilesToSet) {
        this.tiles = tilesToSet;
    }

    public final void setEntities(final Array<Entity> entitiesToSet) {
        this.entities = entitiesToSet;
    }

    public final void addEntity(final Entity entityToAdd) {
        this.entities.add(entityToAdd);
    }

    public final void removeEntity(final Entity entityToRemove) {
        this.entities.removeValue(entityToRemove, true);
    }

    public final void reset() {
        this.tiles.clear();
        this.entities.clear();
    }
}
