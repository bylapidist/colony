package net.lapidist.colony.client.systems.assets;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import net.lapidist.colony.components.AssetComponent;
import net.lapidist.colony.components.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.systems.abstracts.AbstractAssetSystem;
import net.lapidist.colony.core.utils.io.FileLocation;

public class MapAssetSystem extends AbstractAssetSystem {

    private final String[] textures = {
            "player",
            "dirt",
            "grass"
    };

    private boolean initialised;

    public MapAssetSystem(final FileLocation fileLocation) {
        super(fileLocation);
    }

    @Override
    protected void initializeGui() {
    }

    @Override
    protected final void initializeMap() {
        for (String texture : textures) {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(AssetComponent.class)
                    .add(TextureComponent.class)
                    .build(world));

            register(texture, e);
        }
    }

    @Override
    protected void processGui(final int entityId) {
    }

    @Override
    protected final void processMap(final int entityId) {
        if (isLoaded() && !initialised) {
            initialised = true;
            MessageManager.getInstance().dispatchMessage(
                    0,
                    null,
                    Events.MAP_INIT
            );
        }
    }
}
