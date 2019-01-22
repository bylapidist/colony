package net.lapidist.colony.core.systems.map;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import net.lapidist.colony.components.assets.AssetComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.io.FileLocation;
import net.lapidist.colony.core.systems.abstracts.AbstractAssetSystem;

public class MapAssetSystem extends AbstractAssetSystem {

    private boolean initialised;

    public MapAssetSystem(FileLocation fileLocation) {
        super(fileLocation);
    }

    @Override
    protected void initialize() {
        super.initialize();

        final String[] textures = {
                "player",
                "empty",
                "dirt"
        };

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
            Events.fire(new MapInitEvent());
        }
    }
}
