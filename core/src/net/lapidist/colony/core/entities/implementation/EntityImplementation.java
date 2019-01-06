package net.lapidist.colony.core.entities.implementation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.core.entities.*;
import net.lapidist.colony.core.entities.buildings.Building;
import net.lapidist.colony.core.entities.terrain.Terrain;
import net.lapidist.colony.core.entities.units.Unit;

public class EntityImplementation {

    private final PooledEngine engine;
    private final EntityType entityType;
    private final TerrainType terrainType;
    private final UnitType unitType;
    private final BuildingType buildingType;
    private final ITile tile;
    private Entity entity;

    public EntityImplementation(final EntityBuilder builder) {
        this.engine = builder.getEngine();
        this.entityType = builder.getEntityType();
        this.terrainType = builder.getTerrainType();
        this.unitType = builder.getUnitType();
        this.buildingType = builder.getBuildingType();
        this.tile = builder.getTile();

        try {
            create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create() throws Exception {
        switch (this.entityType) {
            case TERRAIN:
                if (terrainType == null) throw new Exception("terrainType is null");

                this.entity = new Terrain(terrainType).create(engine, tile);
                break;

            case UNIT:
                if (unitType == null) throw new Exception("unitType is null");

                this.entity = new Unit(unitType).create(engine, tile);
                break;

            case BUILDING:
                if (buildingType == null) throw new Exception("buildingType is null");

//                this.entity = new Building(buildingType).create(engine, tile);
                break;
        }
    }

    public Entity getEntity() throws Exception {
        if (entity == null) throw new Exception("entity is null");

        return entity;
    }
}
