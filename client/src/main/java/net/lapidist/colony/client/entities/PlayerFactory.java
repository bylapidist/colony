package net.lapidist.colony.client.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.components.state.ResourceData;

/**
 * Factory methods for creating the player entity once at startup.
 */
public final class PlayerFactory {
    private static final float DEFAULT_LIGHT_RADIUS = 3f;
    private static final float DEFAULT_LIGHT_INTENSITY = 0.6f;
    private static final com.badlogic.gdx.graphics.Color DEFAULT_LIGHT_COLOR =
            new com.badlogic.gdx.graphics.Color(1f, 0.6f, 0.2f, 1f);

    private PlayerFactory() {
    }

    /**
     * Create the player entity.
     *
     * @param world    Artemis world
     * @param client   optional game client for map dimensions
     * @param resources initial resources
     * @param position starting tile position
     * @return created entity
     */
    public static Entity create(
            final World world,
            final GameClient client,
            final ResourceData resources,
            final PlayerPosition position
    ) {
        Entity player = world.createEntity();
        PlayerResourceComponent pr = new PlayerResourceComponent();
        if (resources != null) {
            pr.setAmount("WOOD", resources.wood());
            pr.setAmount("STONE", resources.stone());
            pr.setAmount("FOOD", resources.food());
        }
        PlayerComponent pc = new PlayerComponent();
        int width = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        int height = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;
        Vector2 pos = position != null
                ? CameraUtils.tileCoordsToWorldCoords(position.x(), position.y())
                : CameraUtils.getWorldCenter(width, height);
        pc.setX(pos.x);
        pc.setY(pos.y);
        PointLightComponent light = new PointLightComponent();
        light.setRadius(DEFAULT_LIGHT_RADIUS);
        light.setIntensity(DEFAULT_LIGHT_INTENSITY);
        light.setColor(DEFAULT_LIGHT_COLOR);
        player.edit().add(pr).add(pc).add(light);
        return player;
    }
}
