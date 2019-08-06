package net.lapidist.colony.core.systems.assets;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import net.lapidist.colony.components.assets.AssetComponent;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.utils.io.FileLocation;
import net.lapidist.colony.core.systems.AbstractAssetSystem;

public class GuiAssetSystem extends AbstractAssetSystem {

    private boolean initialised;
    private final String[] fonts = {
            "default"
    };
    private final String[] textures = {
            "hoveredTile"
    };

    public GuiAssetSystem(FileLocation fileLocation) {
        super(fileLocation);
    }

    @Override
    protected void initialize() {
        super.initialize();

        for (String font: fonts) {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(AssetComponent.class)
                    .add(FontComponent.class)
                    .build(world));

            register(font, e);
        }

        for (String texture: textures) {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(AssetComponent.class)
                    .add(TextureComponent.class)
                    .build(world));

            register(texture, e);
        }
    }

    @Override
    protected void process(int entityId) {
        super.process(entityId);

        if (loaded && !initialised) {
            initialised = true;
            Events.fire(new GuiInitEvent());
        }
    }
}
