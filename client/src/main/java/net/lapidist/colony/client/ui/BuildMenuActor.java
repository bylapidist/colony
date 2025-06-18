package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.settings.GraphicsSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Menu listing available buildings to construct.
 */
public final class BuildMenuActor extends Table implements Disposable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildMenuActor.class);
    private static final float ICON_SIZE = 32f;
    private static final float PAD = 4f;
    private static final float RESOURCE_SIZE = 16f;

    private final BuildPlacementSystem buildSystem;
    private final ResourceLoader resourceLoader = new TextureAtlasResourceLoader();
    private final Skin skin;
    private final Texture woodTexture;
    private final Texture stoneTexture;
    private final Texture foodTexture;

    public BuildMenuActor(
            final Skin skinToUse,
            final BuildPlacementSystem buildSystemToUse,
            final GraphicsSettings graphics
    ) {
        this.buildSystem = buildSystemToUse;
        this.skin = skinToUse;
        setName("buildMenu");
        setFillParent(true);
        bottom().left();
        setVisible(false);

        try {
            resourceLoader.loadTextures(
                    FileLocation.INTERNAL,
                    "textures/textures.atlas",
                    graphics
            );
            resourceLoader.finishLoading();
        } catch (IOException e) {
            LOGGER.warn("Failed to load build menu textures", e);
        }

        woodTexture = new Texture(resourceLoader.getFileLocation().getFile("textures/wood.png"));
        stoneTexture = new Texture(resourceLoader.getFileLocation().getFile("textures/stone.png"));
        foodTexture = new Texture(resourceLoader.getFileLocation().getFile("textures/food.png"));

        ButtonGroup<Button> group = new ButtonGroup<>();

        for (BuildingDefinition def : Registries.buildings().all()) {
            Button button = new Button(skin, "toggle");
            button.setName(def.id());

            Table content = new Table();
            TextureRegion region = resourceLoader.findRegion(def.asset());
            if (region != null) {
                Image icon = new Image(new TextureRegionDrawable(region));
                content.add(icon).size(ICON_SIZE, ICON_SIZE).padRight(PAD);
            }

            Table info = new Table();
            Label label = new Label(def.label(), skin);
            info.add(label).padRight(PAD);

            if (def.cost().wood() > 0) {
                Image woodIcon = new Image(new TextureRegionDrawable(woodTexture));
                woodIcon.setName("woodIcon");
                info.add(woodIcon).size(RESOURCE_SIZE, RESOURCE_SIZE).padRight(PAD / 2f);
                info.add(new Label(String.valueOf(def.cost().wood()), skin)).padRight(PAD);
            }
            if (def.cost().stone() > 0) {
                Image stoneIcon = new Image(new TextureRegionDrawable(stoneTexture));
                stoneIcon.setName("stoneIcon");
                info.add(stoneIcon).size(RESOURCE_SIZE, RESOURCE_SIZE).padRight(PAD / 2f);
                info.add(new Label(String.valueOf(def.cost().stone()), skin)).padRight(PAD);
            }
            if (def.cost().food() > 0) {
                Image foodIcon = new Image(new TextureRegionDrawable(foodTexture));
                foodIcon.setName("foodIcon");
                info.add(foodIcon).size(RESOURCE_SIZE, RESOURCE_SIZE).padRight(PAD / 2f);
                info.add(new Label(String.valueOf(def.cost().food()), skin));
            }

            content.add(info);
            button.add(content).pad(PAD);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    buildSystem.setSelectedBuilding(def.id());
                }
            });
            group.add(button);
            String current = buildSystem.getSelectedBuilding();
            if (current != null && def.id().equalsIgnoreCase(current)) {
                button.setChecked(true);
            }
            button.addListener(createTooltip(def));
            add(button).pad(PAD).row();
        }
        // no further selection
    }

    private Tooltip<Label> createTooltip(final BuildingDefinition def) {
        StringBuilder text = new StringBuilder(def.description() == null ? "" : def.description());
        String cost = formatCost(def.cost());
        if (!cost.isEmpty()) {
            if (text.length() > 0) {
                text.append('\n');
            }
            text.append(cost);
        }
        Tooltip<Label> tooltip = new Tooltip<>(new Label(text.toString(), skin));
        tooltip.setInstant(true);
        return tooltip;
    }

    private static String formatCost(final net.lapidist.colony.components.state.ResourceData cost) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        if (cost.wood() > 0) {
            parts.add(cost.wood() + " wood");
        }
        if (cost.stone() > 0) {
            parts.add(cost.stone() + " stone");
        }
        if (cost.food() > 0) {
            parts.add(cost.food() + " food");
        }
        return String.join(", ", parts);
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        woodTexture.dispose();
        stoneTexture.dispose();
        foodTexture.dispose();
    }
}
