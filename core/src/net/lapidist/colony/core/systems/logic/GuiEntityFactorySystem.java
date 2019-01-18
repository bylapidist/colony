package net.lapidist.colony.core.systems.logic;

import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import net.lapidist.colony.components.DimensionsComponent;
import net.lapidist.colony.components.NameComponent;
import net.lapidist.colony.components.render.GuiComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.core.Colony;

import static com.artemis.E.E;

public class GuiEntityFactorySystem extends BaseSystem {

    public Entity createEntity(String entity, float cx, float cy, MapProperties properties) {
        switch (entity) {
            case "hoveredTile":
                return createHoveredTile(cx, cy, properties);
            default:
                throw new RuntimeException("Unknown gui entity type");
        }
    }

    private ArchetypeBuilder createGuiArchetype() {
        return new ArchetypeBuilder()
                .add(DimensionsComponent.class)
                .add(NameComponent.class)
                .add(SpriteComponent.class)
                .add(GuiComponent.class);
    }

    private Entity createHoveredTile(float cx, float cy, MapProperties properties) {
        Entity entity = world.createEntity(createGuiArchetype().build(world));
        Sprite sprite = new Sprite(Colony.getResourceLoader().getTexture("empty"));
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
                .spriteComponentSprite(sprite);

        return entity;
    }

    @Override
    protected void processSystem() {

    }
}
