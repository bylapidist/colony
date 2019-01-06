package net.lapidist.colony.core.entities.terrain;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.core.entities.TerrainType;

public class Terrain extends Entity {

    private PooledEngine engine;
    private TerrainType terrainType;
    private ITile tile;

    Terrain() {
    }

    public Terrain(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public Entity create(PooledEngine engine, ITile tile) {
        this.engine = engine;
        this.tile = tile;

        switch (terrainType) {
            case GRASS:
                return new GrassTerrain(this);
        }

        return null;
    }

    PooledEngine getEngine() {
        return engine;
    }

    public ITile getTile() {
        return tile;
    }

    public void setTile(ITile tile) {
        this.tile = tile;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
}
