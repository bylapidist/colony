package net.lapidist.colony.client.ui;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.lapidist.colony.components.resources.PlayerResourceComponent;

/** Displays the player's resource counts. */
public final class PlayerResourcesActor extends Table {
    private final World world;
    private final Label label;
    private ComponentMapper<PlayerResourceComponent> mapper;
    private Entity player;

    public PlayerResourcesActor(final Skin skin, final World worldToUse) {
        this.world = worldToUse;
        this.label = new Label("", skin);
        add(label);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (mapper == null) {
            mapper = world.getMapper(PlayerResourceComponent.class);
        }
        if (player == null) {
            var players = world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerResourceComponent.class))
                    .getEntities();
            if (players.size() > 0) {
                player = world.getEntity(players.get(0));
            }
        }
        if (player != null) {
            PlayerResourceComponent pr = mapper.get(player);
            label.setText("W:" + pr.getWood() + " S:" + pr.getStone() + " F:" + pr.getFood());
        }
    }
}
