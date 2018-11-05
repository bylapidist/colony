package net.lapidist.colony.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import net.lapidist.colony.common.grid.hex.IHexagon;

public class TileComponent implements Component {

    public IHexagon hex;
    public Polygon bounds;
    public boolean active;
    public boolean hovered;
}
