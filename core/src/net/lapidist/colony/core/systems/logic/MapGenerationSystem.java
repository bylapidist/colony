package net.lapidist.colony.core.systems.logic;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ObjectMap;
import net.lapidist.colony.core.Colony;

@Wire
public class MapGenerationSystem extends BaseSystem {

    private EntityFactorySystem entityFactorySystem;
    private boolean isSetup;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    public TiledMap map;
    public ObjectMap<String, TiledMapTileLayer> layers;

    public MapGenerationSystem(int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    protected void initialize() {
        map = new TiledMap();
        layers = new ObjectMap<>();
    }

    @Override
    protected void processSystem() {
        if (!isSetup) {
            setup();

            isSetup = true;
        }
    }

    private TiledMapTileLayer createLayer(String name) {
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
        layer.setName(name);

        return layer;
    }

    private void createTerrainLayer() {
        TiledMapTileLayer layer = createLayer("terrainLayer");
        layers.put("terrainLayer", layer);

        for (int ty = 0; ty < height; ty++) {
            for (int tx = 0; tx < width; tx++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                cell.setTile(new StaticTiledMapTile(Colony.getResourceLoader().getTexture("dirt")));
                cell.getTile().getProperties().put("entity", "terrain");
                cell.getTile().getProperties().put("tileWidth", tileWidth);
                cell.getTile().getProperties().put("tileHeight", tileHeight);

                layers.get("terrainLayer").setCell(tx, ty, cell);
            }
        }
    }

    private void createUnitLayer() {
        TiledMapTileLayer layer = createLayer("unitsLayer");

        layers.put("unitsLayer", layer);
    }

    private void createBuildingLayer() {
        TiledMapTileLayer layer = createLayer("buildingsLayer");

        layers.put("buildingsLayer", layer);
    }

    private void createPlayer() {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        cell.setTile(new StaticTiledMapTile(Colony.getResourceLoader().getTexture("player")));
        cell.getTile().getProperties().put("entity", "player");
        cell.getTile().getProperties().put("tileWidth", tileWidth);
        cell.getTile().getProperties().put("tileHeight", tileHeight);

        layers.get("unitsLayer").setCell(2, 2, cell);
    }

    private void setup() {
        createTerrainLayer();
        createBuildingLayer();
        createUnitLayer();
        createPlayer();

        for (TiledMapTileLayer layer: layers.values()) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);

                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();

                        if (properties.containsKey("entity")) {
                            entityFactorySystem.createEntity(
                                    (String) properties.get("entity"),
                                    tx * tileWidth,
                                    ty * tileHeight,
                                    properties,
                                    cell
                            );
                        }
                    }
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
