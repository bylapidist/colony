package net.lapidist.colony.core.systems;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.artemis.World;
import net.lapidist.colony.components.*;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.components.render.UpdatableComponent;

public class EntityFactory {

    private ArchetypeBuilder createBuildingArchetype() {
        return new ArchetypeBuilder()
                .add(BuildingComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(OrientationComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(TileComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createTerrainArchetype() {
        return new ArchetypeBuilder()
                .add(TerrainComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(OrientationComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(TileComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createUnitArchetype() {
        return new ArchetypeBuilder()
                .add(UnitComponent.class)
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(OrientationComponent.class)
                .add(RenderableComponent.class)
                .add(SpriteComponent.class)
                .add(TileComponent.class)
                .add(UpdatableComponent.class);
    }

    private ArchetypeBuilder createPlayerArchetype() {
        return createUnitArchetype()
                .add(PlayerComponent.class);
    }

    public Entity createBuilding(World world) {
        return world.createEntity(createBuildingArchetype().build(world));
    }

    public Entity createTerrain(World world) {
        return world.createEntity(createTerrainArchetype().build(world));
    }

    public Entity createUnit(World world) {
        return world.createEntity(createUnitArchetype().build(world));
    }

    public Entity createPlayer(World world) {
        return world.createEntity(createPlayerArchetype().build(world));
    }
}
