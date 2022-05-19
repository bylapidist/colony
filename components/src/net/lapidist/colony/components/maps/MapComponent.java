package net.lapidist.colony.components.maps;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

public class MapComponent extends Component {

    private Array<Entity> tiles;

    public final Array<Entity> getTiles() {
        return tiles;
    }

    public final void setTiles(final Array<Entity> tilesToSet) {
        this.tiles = tilesToSet;
    }
}
