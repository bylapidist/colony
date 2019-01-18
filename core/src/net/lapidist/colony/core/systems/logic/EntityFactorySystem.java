package net.lapidist.colony.core.systems.logic;

import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.lapidist.colony.components.*;
import net.lapidist.colony.components.archetypes.TerrainType;
import net.lapidist.colony.components.archetypes.UnitType;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.components.render.UpdatableComponent;

import static com.artemis.E.E;

public class EntityFactorySystem extends BaseSystem {

    public Entity createEntity(String entity, float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
        switch (entity) {
            case "building":
                return createBuilding(cx, cy, properties);
            case "terrain":
                return createTerrain(cx, cy, properties, cell);
            case "unit":
                return createUnit(cx, cy, properties);
            case "player":
                return createPlayer(cx, cy, properties, cell);
            default:
                throw new RuntimeException("Unknown entity type");
        }
    }

    private ArchetypeBuilder createBuildingArchetype() {
        return new ArchetypeBuilder()
                .add(BuildingComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createTerrainArchetype() {
        return new ArchetypeBuilder()
                .add(TerrainComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createUnitArchetype() {
        return new ArchetypeBuilder()
                .add(UnitComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createPlayerArchetype() {
        return createUnitArchetype()
                .add(PlayerComponent.class);
    }

    private Entity createBuilding(float cx, float cy, MapProperties properties) {
        Entity entity = world.createEntity(createBuildingArchetype().build(world));

        return entity;
    }

    private Entity createTerrain(float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
        Entity entity = world.createEntity(createTerrainArchetype().build(world));
        Sprite sprite = new Sprite(cell.getTile().getTextureRegion());
        sprite.setBounds(
                cx,
                cy,
                properties.get("tileWidth", Integer.class),
                properties.get("tileHeight", Integer.class)
        );

        E(entity).nameComponentName((String) properties.get("entity"))
                .spriteComponentSprite(sprite)
                .terrainComponentTerrainType(TerrainType.EMPTY);

        return entity;
    }

    private Entity createUnit(float cx, float cy, MapProperties properties) {
        Entity entity = world.createEntity(createUnitArchetype().build(world));

        return entity;
    }

    private Entity createPlayer(float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
        Entity entity = world.createEntity(createPlayerArchetype().build(world));
        Sprite sprite = new Sprite(cell.getTile().getTextureRegion());
        sprite.setBounds(
                cx,
                cy,
                properties.get("tileWidth", Integer.class),
                properties.get("tileHeight", Integer.class)
        );

        E(entity).nameComponentName((String) properties.get("entity"))
                .spriteComponentSprite(sprite)
                .unitComponentUnitType(UnitType.PLAYER);

        return entity;
    }

    @Override
    protected void processSystem() {

    }
}
