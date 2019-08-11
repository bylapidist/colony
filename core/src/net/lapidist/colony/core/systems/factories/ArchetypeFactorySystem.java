package net.lapidist.colony.core.systems.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.*;

@Wire
public final class ArchetypeFactorySystem extends BaseSystem {

    public int create(Archetype archetype) {
        return world.create(archetype);
    }

    public Archetype getArchetype(Archetypes type) {
        switch (type) {
            case TILE:
                return createTile().build(world);
            case CHUNK:
                return createChunk().build(world);
            case ITEM:
                return createItem().build(world);
            case TERRAIN:
                return createTerrain().build(world);
            case BUILDING:
                return createBuilding().build(world);
            case UNIT:
                return createUnit().build(world);
            case COLLECTOR:
                return createCollector().build(world);
            case LABEL:
                return createLabel().build(world);
        }

        throw new RuntimeException("Unknown archetype");
    }

    private ArchetypeBuilder createTile() {
        return new ArchetypeBuilder()
                .add(TileComponent.class)
                .add(WorldPositionComponent.class)
                .add(OriginComponent.class);
    }

    private ArchetypeBuilder createChunk() {
        return new ArchetypeBuilder()
                .add(ChunkComponent.class)
                .add(WorldPositionComponent.class)
                .add(OriginComponent.class);
    }

    private ArchetypeBuilder createItem() {
        return createSortable()
                .add(ItemComponent.class);
    }

    private ArchetypeBuilder createUnit() {
        return createSortable()
                .add(UnitComponent.class);
    }

    private ArchetypeBuilder createCollector() {
        return createSortable()
                .add(UnitComponent.class)
                .add(CollectorComponent.class);
    }

    private ArchetypeBuilder createBuilding() {
        return createSortable()
                .add(BuildingComponent.class);
    }

    private ArchetypeBuilder createTerrain() {
        return createSortable()
                .add(TerrainComponent.class);
    }

    private ArchetypeBuilder createSortable() {
        return new ArchetypeBuilder()
                .add(TextureComponent.class)
                .add(RotationComponent.class)
                .add(OriginComponent.class)
                .add(WorldPositionComponent.class)
                .add(VelocityComponent.class)
                .add(ScaleComponent.class)
                .add(SortableComponent.class);
    }

    private ArchetypeBuilder createLabel() {
        return createSortable()
                .add(FontComponent.class)
                .add(LabelComponent.class)
                .add(GuiComponent.class);
    }

    @Override
    protected void processSystem() {

    }
}
