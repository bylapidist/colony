package net.lapidist.colony.core.components;

import com.badlogic.ashley.core.Component;
import net.lapidist.colony.common.map.tile.ITile;

public class TileComponent implements Component {

    public ITile tile;
    public boolean active;
    public boolean hovered;
}
