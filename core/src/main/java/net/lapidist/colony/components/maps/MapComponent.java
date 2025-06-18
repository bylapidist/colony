package net.lapidist.colony.components.maps;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.components.state.map.TilePos;

import java.util.Map;

public class MapComponent extends Component {

    private int version;

    private Array<Entity> tiles;

    private Map<TilePos, Entity> tileMap;

    private Array<Entity> entities;

    /**
     * Returns the current modification counter for this map. Render systems use
     * this value to detect changes.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Increments the modification counter to signal that the map state has
     * changed.
     */
    public void incrementVersion() {
        this.version++;
    }

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
        incrementVersion();
    }

    public final void removeEntity(final Entity entityToRemove) {
        this.entities.removeValue(entityToRemove, true);
        incrementVersion();
    }

    public final void reset() {
        this.tiles.clear();
        if (this.tileMap != null) {
            this.tileMap.clear();
        }
        this.entities.clear();
        incrementVersion();
    }
}
