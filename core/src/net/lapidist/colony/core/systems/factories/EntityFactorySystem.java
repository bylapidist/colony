package net.lapidist.colony.core.systems.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.*;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
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
            case "terrain":
                return createTerrain().build(world);
            case "label":
                return createLabel().build(world);
            case "player":
                return createPlayer().build(world);
        }

        throw new RuntimeException("Unknown archetype");
    }

    private ArchetypeBuilder createTile() {
        return new ArchetypeBuilder()
                .add(TileComponent.class)
                .add(PositionComponent.class)
                .add(OriginComponent.class);
    }

    private ArchetypeBuilder createTerrain() {
        return new ArchetypeBuilder()
                .add(TextureComponent.class)
                .add(RotationComponent.class)
                .add(OriginComponent.class)
                .add(PositionComponent.class)
                .add(ScaleComponent.class)
                .add(SortableComponent.class);
    }

    private ArchetypeBuilder createLabel() {
        return new ArchetypeBuilder()
                .add(PositionComponent.class)
                .add(FontComponent.class)
                .add(LabelComponent.class)
                .add(GuiComponent.class)
                .add(SortableComponent.class);
    }

    private ArchetypeBuilder createPlayer() {
        return new ArchetypeBuilder()
                .add(PlayerComponent.class)
                .add(TextureComponent.class)
                .add(RotationComponent.class)
                .add(PositionComponent.class)
                .add(OriginComponent.class)
                .add(VelocityComponent.class)
                .add(ScaleComponent.class)
                .add(DynamicBodyComponent.class);
    }

    @Override
    protected void processSystem() {

    }
}
