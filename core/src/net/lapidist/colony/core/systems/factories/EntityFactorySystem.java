package net.lapidist.colony.core.systems.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.*;
import net.lapidist.colony.components.building.BuildingComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.components.items.ItemComponent;
import net.lapidist.colony.components.map.ChunkComponent;
import net.lapidist.colony.components.map.TerrainComponent;
import net.lapidist.colony.components.map.TileComponent;
import net.lapidist.colony.components.player.PlayerComponent;

@Wire
public final class EntityFactorySystem extends BaseSystem {

    public int create(Archetype archetype) {
        return world.create(archetype);
    }

    public Archetype getArchetype(String type) {
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
            case "building":
                return createBuilding().build(world);
            case "hoveredTile":
                return createSortable().build(world);
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

    private ArchetypeBuilder createBuilding() {
        return createSortable()
                .add(BuildingComponent.class)
                .add(DynamicBodyComponent.class);
    }

    @Override
    protected void processSystem() {

    }
}
