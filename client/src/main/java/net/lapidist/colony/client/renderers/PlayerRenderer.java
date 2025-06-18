package net.lapidist.colony.client.renderers;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.entities.PlayerComponent;

/** Draws the player sprite at the player component position. */
public final class PlayerRenderer implements EntityRenderer<Void> {
    private final SpriteBatch spriteBatch;
    private final ComponentMapper<PlayerComponent> playerMapper;
    private final World world;
    private Entity player;
    private final TextureRegion region;

    public PlayerRenderer(
            final SpriteBatch batch,
            final ResourceLoader loader,
            final World worldContext
    ) {
        this.spriteBatch = batch;
        this.world = worldContext;
        this.playerMapper = worldContext.getMapper(PlayerComponent.class);
        this.region = loader.findRegion("player0");
    }

    @Override
    public void render(final net.lapidist.colony.client.render.MapRenderData map) {
        if (region == null) {
            return;
        }
        if (player == null) {
            var players = world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerComponent.class))
                    .getEntities();
            if (players.size() == 0) {
                return;
            }
            player = world.getEntity(players.get(0));
        }
        PlayerComponent pc = playerMapper.get(player);
        float drawX = pc.getX() - region.getRegionWidth() / 2f;
        float drawY = pc.getY() - region.getRegionHeight() / 2f;
        spriteBatch.draw(region, drawX, drawY);
    }

    // PlayerRenderer does not own disposable resources
}
