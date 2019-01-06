package net.lapidist.colony.core.entities.units;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.core.entities.UnitType;

public class Unit extends Entity {

    private PooledEngine engine;
    private UnitType unitType;
    private ITile tile;

    Unit() {
    }

    public Unit(UnitType unitType) {
        this.unitType = unitType;
    }

    public Entity create(PooledEngine engine, ITile tile) {
        this.engine = engine;
        this.tile = tile;

        switch (unitType) {
            case PLAYER:
                return new PlayerUnit(this);
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
}
