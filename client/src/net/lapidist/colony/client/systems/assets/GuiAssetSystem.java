package net.lapidist.colony.client.systems.assets;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import net.lapidist.colony.components.AssetComponent;
import net.lapidist.colony.components.FontComponent;
import net.lapidist.colony.components.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.systems.abstracts.AbstractAssetSystem;
import net.lapidist.colony.core.utils.io.FileLocation;

public class GuiAssetSystem extends AbstractAssetSystem {

    private final String[] fonts = {
            "default"
    };

    private final String[] textures = {
            "hoveredTile"
    };

    private boolean initialised;

    public GuiAssetSystem(final FileLocation fileLocation) {
        super(fileLocation);
    }

    @Override
    protected final void initializeGui() {
        for (String font : fonts) {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(AssetComponent.class)
                    .add(FontComponent.class)
                    .build(world));

            register(font, e);
        }

        for (String texture : textures) {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(AssetComponent.class)
                    .add(TextureComponent.class)
                    .build(world));

            register(texture, e);
        }
    }

    @Override
    protected void initializeMap() {
    }

    @Override
    protected final void processGui(final int entityId) {
        if (isLoaded() && !initialised) {
            initialised = true;
            MessageManager.getInstance().dispatchMessage(
                    0,
                    null,
                    Events.GUI_INIT
            );
        }
    }

    @Override
    protected void processMap(final int entityId) {
    }
}
