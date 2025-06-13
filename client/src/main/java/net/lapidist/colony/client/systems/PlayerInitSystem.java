package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.state.PlayerPosition;

/** Creates a player entity with resource storage. */
public final class PlayerInitSystem extends BaseSystem {
    private boolean created;
    private final ResourceData initialResources;
    private final PlayerPosition initialPosition;

    public PlayerInitSystem() {
        this(new ResourceData(), null);
    }

    public PlayerInitSystem(final ResourceData resources) {
        this(resources, null);
    }

    public PlayerInitSystem(final ResourceData resources, final PlayerPosition position) {
        this.initialResources = resources;
        this.initialPosition = position;
    }

    @Override
    public void initialize() {
        if (!created) {
            Entity player = world.createEntity();
            PlayerResourceComponent pr = new PlayerResourceComponent();
            pr.setWood(initialResources.wood());
            pr.setStone(initialResources.stone());
            pr.setFood(initialResources.food());
            PlayerComponent pc = new PlayerComponent();
            var pos = initialPosition != null
                    ? CameraUtils.tileCoordsToWorldCoords(initialPosition.x(), initialPosition.y())
                    : CameraUtils.getWorldCenter();
            pc.setX(pos.x);
            pc.setY(pos.y);
            player.edit().add(pr).add(pc);
            created = true;
        }
    }

    @Override
    protected void processSystem() {
        // no-op
    }
}
