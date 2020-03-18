package net.lapidist.colony.core.systems.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.*;

@Wire
public final class EntityFactorySystem extends BaseSystem {

    public int create(final Archetype archetype) {
        return world.create(archetype);
    }

    public Archetype getArchetype(final String type) {
        switch (type) {
            case "tile":
                return createTile().build(world);
            case "chunk":
                return createChunk().build(world);
            case "item":
                return createItem().build(world);
            case "terrain":
                return createTerrain().build(world);
            case "label":
                return createLabel().build(world);
            case "player":
                return createPlayer().build(world);
            case "hoveredTile":
                return createSortable().build(world);
            default:
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

    private ArchetypeBuilder createTerrain() {
        return createSortable()
                .add(TerrainComponent.class);
    }

    private ArchetypeBuilder createLabel() {
        return createSortable()
                .add(FontComponent.class)
                .add(LabelComponent.class)
                .add(GuiComponent.class);
    }

    private ArchetypeBuilder createPlayer() {
        return createSortable()
                .add(PlayerComponent.class)
                .add(DynamicBodyComponent.class)
                .add(InventoryComponent.class);
    }

    @Override
    protected void processSystem() {

    }
}
