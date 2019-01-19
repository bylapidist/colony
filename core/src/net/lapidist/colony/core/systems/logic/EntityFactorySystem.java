package net.lapidist.colony.core.systems.logic;

import box2dLight.PointLight;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.lapidist.colony.components.*;
import net.lapidist.colony.components.archetypes.TerrainType;
import net.lapidist.colony.components.archetypes.UnitType;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.components.render.UpdatableComponent;
import net.lapidist.colony.core.systems.render.LightRenderingSystem;

import static com.artemis.E.E;

@Wire
public class EntityFactorySystem extends BaseSystem {

    private LightFactorySystem lightFactorySystem;
    private LightRenderingSystem lightRenderingSystem;

    public Entity createEntity(String entity, float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
        switch (entity) {
            case "building":
                return createBuilding(cx, cy, properties, cell);
            case "terrain":
                return createTerrain(cx, cy, properties, cell);
            case "unit":
                return createUnit(cx, cy, properties, cell);
            case "player":
                return createPlayer(cx, cy, properties, cell);
            default:
                throw new RuntimeException("Unknown entity type");
        }
    }

    private ArchetypeBuilder createCellArchetype() {
        return new ArchetypeBuilder()
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(CellComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createBuildingArchetype() {
        return createCellArchetype()
                .add(BuildingComponent.class);
    }

    private ArchetypeBuilder createTerrainArchetype() {
        return createCellArchetype()
                .add(TerrainComponent.class);
    }

    private ArchetypeBuilder createUnitArchetype() {
        return createCellArchetype()
                .add(UnitComponent.class);
    }

    private ArchetypeBuilder createPlayerArchetype() {
        return createUnitArchetype()
                .add(PlayerComponent.class)
                .add(VelocityComponent.class)
                .add(PointLightComponent.class)
                .add(DynamicBodyComponent.class);
    }

    private Entity createBuilding(float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
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
                .cellComponentCell(cell)
                .terrainComponentTerrainType(TerrainType.EMPTY);

        return entity;
    }

    private Entity createUnit(float cx, float cy, MapProperties properties, TiledMapTileLayer.Cell cell) {
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
        sprite.setOrigin(
                cx + (properties.get("tileWidth", Integer.class) / 2f),
                cy + (properties.get("tileHeight", Integer.class) / 2f)
        );

        E(entity).nameComponentName((String) properties.get("entity"))
                .spriteComponentSprite(sprite)
                .cellComponentCell(cell)
                .unitComponentUnitType(UnitType.PLAYER);

        E(entity).dynamicBodyComponentBodyDef().position.set(sprite.getOriginX(), sprite.getOriginY());
        E(entity).dynamicBodyComponentBody(lightRenderingSystem.getPhysicsWorld().createBody(
                E(entity).dynamicBodyComponentBodyDef()
        ));

        PointLight pl = lightFactorySystem.createPointlight(
                lightRenderingSystem.getRayHandler(),
                E(entity).dynamicBodyComponentBody(),
                new Color(1f,1,1,0.3f),
                10
        );

        PointLight pl2 = lightFactorySystem.createPointlight(
                lightRenderingSystem.getRayHandler(),
                E(entity).dynamicBodyComponentBody(),
                new Color(  1f,1,1,0.4f),
                8
        );

        E(entity).pointLightComponentPointLights().add(pl, pl2);

        return entity;
    }

    @Override
    protected void processSystem() {

    }
}
