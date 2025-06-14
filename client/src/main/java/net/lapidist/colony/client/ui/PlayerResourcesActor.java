package net.lapidist.colony.client.ui;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.lapidist.colony.components.resources.PlayerResourceComponent;

/** Displays the player's resource counts. */
public final class PlayerResourcesActor extends Table {
    private static final float ICON_SIZE = 16f;
    private static final float PAD_SMALL = 2f;
    private static final float PAD_LARGE = 6f;

    private final World world;
    private final Label woodLabel;
    private final Label stoneLabel;
    private final Label foodLabel;
    private ComponentMapper<PlayerResourceComponent> mapper;
    private Entity player;
    private final Image woodIcon;
    private final Image stoneIcon;
    private final Image foodIcon;

    public PlayerResourcesActor(final Skin skin, final World worldToUse) {
        this.world = worldToUse;

        woodLabel = new Label("", skin);
        stoneLabel = new Label("", skin);
        foodLabel = new Label("", skin);

        woodIcon = new Image(new TextureRegionDrawable(new Texture(Gdx.files.internal("textures/wood.png"))));
        stoneIcon = new Image(new TextureRegionDrawable(new Texture(Gdx.files.internal("textures/stone.png"))));
        foodIcon = new Image(new TextureRegionDrawable(new Texture(Gdx.files.internal("textures/food.png"))));

        woodIcon.setSize(ICON_SIZE, ICON_SIZE);
        stoneIcon.setSize(ICON_SIZE, ICON_SIZE);
        foodIcon.setSize(ICON_SIZE, ICON_SIZE);

        add(woodIcon).size(ICON_SIZE, ICON_SIZE).padRight(PAD_SMALL);
        add(woodLabel).padRight(PAD_LARGE);
        add(stoneIcon).size(ICON_SIZE, ICON_SIZE).padRight(PAD_SMALL);
        add(stoneLabel).padRight(PAD_LARGE);
        add(foodIcon).size(ICON_SIZE, ICON_SIZE).padRight(PAD_SMALL);
        add(foodLabel);
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
            woodLabel.setText(String.valueOf(pr.getWood()));
            stoneLabel.setText(String.valueOf(pr.getStone()));
            foodLabel.setText(String.valueOf(pr.getFood()));
        }
    }
}
