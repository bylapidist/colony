package net.lapidist.colony.components.maps;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class MapComponent implements Component {

    private Array<Entity> tiles;

    public final Array<Entity> getTiles() {
        return tiles;
    }

    public final void setTiles(final Array<Entity> tilesToSet) {
        this.tiles = tilesToSet;
    }
}
