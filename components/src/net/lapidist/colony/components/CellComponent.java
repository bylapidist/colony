package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CellComponent extends Component {

    private TiledMapTileLayer.Cell cell;

    public CellComponent() {
        this.cell = new TiledMapTileLayer.Cell();
    }

    public CellComponent(TiledMapTileLayer.Cell cell) {
        this();

        setCell(cell);
    }

    public TiledMapTileLayer.Cell getCell() {
        return cell;
    }

    public void setCell(TiledMapTileLayer.Cell cell) {
        this.cell = cell;
    }
}
