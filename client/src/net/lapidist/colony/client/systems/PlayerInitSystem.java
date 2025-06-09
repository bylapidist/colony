package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import net.lapidist.colony.components.resources.PlayerResourceComponent;

/** Creates a player entity with resource storage. */
public final class PlayerInitSystem extends BaseSystem {
    private boolean created;

    @Override
    public void initialize() {
        if (!created) {
            Entity player = world.createEntity();
            player.edit().add(new PlayerResourceComponent());
            created = true;
        }
    }

    @Override
    protected void processSystem() {
        // no-op
    }
}
