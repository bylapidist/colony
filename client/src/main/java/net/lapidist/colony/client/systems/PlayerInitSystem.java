package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.components.state.PlayerPosition;

/** Creates a player entity with resource storage. */
public final class PlayerInitSystem extends BaseSystem {
    private boolean created;
    private final ResourceData initialResources;
    private final PlayerPosition initialPosition;
    private final net.lapidist.colony.client.network.GameClient client;
    private static final float DEFAULT_LIGHT_RADIUS = 3f;
    private static final float DEFAULT_LIGHT_INTENSITY = 0.6f;

    public PlayerInitSystem() {
        this(null, new ResourceData(), null);
    }

    public PlayerInitSystem(final ResourceData resources) {
        this(null, resources, null);
    }

    public PlayerInitSystem(
            final net.lapidist.colony.client.network.GameClient clientToUse,
            final ResourceData resources,
            final PlayerPosition position
    ) {
        this.client = clientToUse;
        this.initialResources = resources;
        this.initialPosition = position;
    }

    @Override
    public void initialize() {
        if (!created) {
            Entity player = world.createEntity();
            PlayerResourceComponent pr = new PlayerResourceComponent();
            pr.setAmount("WOOD", initialResources.wood());
            pr.setAmount("STONE", initialResources.stone());
            pr.setAmount("FOOD", initialResources.food());
            PlayerComponent pc = new PlayerComponent();
            int width = client != null ? client.getMapWidth()
                    : net.lapidist.colony.components.state.MapState.DEFAULT_WIDTH;
            int height = client != null ? client.getMapHeight()
                    : net.lapidist.colony.components.state.MapState.DEFAULT_HEIGHT;
            var pos = initialPosition != null
                    ? CameraUtils.tileCoordsToWorldCoords(initialPosition.x(), initialPosition.y())
                    : CameraUtils.getWorldCenter(width, height);
            pc.setX(pos.x);
            pc.setY(pos.y);
            PointLightComponent light = new PointLightComponent();
            light.setRadius(DEFAULT_LIGHT_RADIUS);
            light.setIntensity(DEFAULT_LIGHT_INTENSITY);
            player.edit().add(pr).add(pc).add(light);
            created = true;
        }
    }

    @Override
    protected void processSystem() {
        // no-op
    }
}
