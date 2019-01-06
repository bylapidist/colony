package net.lapidist.colony.core.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.core.entities.implementation.EntityImplementation;

public class EntityBuilder {

    private PooledEngine engine;
    private EntityType entityType;
    private UnitType unitType;
    private BuildingType buildingType;
    private TerrainType terrainType;
    private ITile tile;

    public Entity build() throws Exception {
        return new EntityImplementation(this).getEntity();
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public EntityBuilder setEntityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public ITile getTile() {
        return tile;
    }

    public EntityBuilder setTile(ITile tile) {
        this.tile = tile;
        return this;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public EntityBuilder setUnitType(UnitType unitType) {
        this.unitType = unitType;
        return this;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public EntityBuilder setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
        return this;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public EntityBuilder setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
        return this;
    }

    public PooledEngine getEngine() {
        return engine;
    }

    public EntityBuilder setEngine(PooledEngine engine) {
        this.engine = engine;
        return this;
    }
}
