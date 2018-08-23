package net.lapidist.colony.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import net.lapidist.colony.grid.hex.IHexagon;

public class TileComponent implements Component {

    public IHexagon hex;
    public Polygon bounds;
    public boolean active;
    public boolean hovered;
}
