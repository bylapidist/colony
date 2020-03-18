package net.lapidist.colony.client.systems.assets;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import net.lapidist.colony.components.AssetComponent;
import net.lapidist.colony.components.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.utils.io.FileLocation;
import net.lapidist.colony.core.systems.abstracts.AbstractAssetSystem;

public class MapAssetSystem extends AbstractAssetSystem {

    private boolean initialised;
    private final String[] textures = {
            "player",
            "dirt",
            "grass"
    };

    public MapAssetSystem(FileLocation fileLocation) {
        super(fileLocation);
    }

    @Override
    protected void initialize() {
        super.initialize();

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
            MessageManager.getInstance().dispatchMessage(0, null, Events.MAP_INIT);
        }
    }
}
