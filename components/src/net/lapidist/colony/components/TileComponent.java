package net.lapidist.colony.components;

import com.artemis.Component;
import net.lapidist.colony.common.map.tile.ITile;

public class TileComponent extends Component {

    private ITile tile;

    public TileComponent() {
    }

    public TileComponent(ITile tile) {
        this();

        setTile(tile);
    }

    public ITile getTile() {
        return tile;
    }

    public void setTile(ITile tile) {
        this.tile = tile;
    }
}
