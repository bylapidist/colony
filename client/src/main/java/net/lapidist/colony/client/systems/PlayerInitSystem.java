package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.ResourceData;

/** Creates a player entity with resource storage. */
public final class PlayerInitSystem extends BaseSystem {
    private boolean created;
    private final ResourceData initialResources;

    public PlayerInitSystem() {
        this(new ResourceData());
    }

    public PlayerInitSystem(final ResourceData resources) {
        this.initialResources = resources;
    }

    @Override
    public void initialize() {
        if (!created) {
            Entity player = world.createEntity();
            PlayerResourceComponent pr = new PlayerResourceComponent();
            pr.setWood(initialResources.wood());
            pr.setStone(initialResources.stone());
            pr.setFood(initialResources.food());
            player.edit().add(pr);
            created = true;
        }
    }

    @Override
    protected void processSystem() {
        // no-op
    }
}
