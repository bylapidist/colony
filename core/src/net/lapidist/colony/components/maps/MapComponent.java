package net.lapidist.colony.components.maps;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.components.state.TilePos;

import java.util.Map;

public class MapComponent extends Component {

    private Array<Entity> tiles;

    private Map<TilePos, Entity> tileMap;

    private Array<Entity> entities;

    public final Array<Entity> getTiles() {
        return tiles;
    }

    public final Map<TilePos, Entity> getTileMap() {
        return tileMap;
    }

    public final Array<Entity> getEntities() {
        return entities;
    }

    public final void setTiles(final Array<Entity> tilesToSet) {
        this.tiles = tilesToSet;
    }

    public final void setTileMap(final Map<TilePos, Entity> tileMapToSet) {
        this.tileMap = tileMapToSet;
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
        if (this.tileMap != null) {
            this.tileMap.clear();
        }
        this.entities.clear();
    }
}
