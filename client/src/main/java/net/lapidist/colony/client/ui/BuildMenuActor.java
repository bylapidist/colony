package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

    private final BuildPlacementSystem buildSystem;
    private final ResourceLoader resourceLoader = new TextureAtlasResourceLoader();

    public BuildMenuActor(
            final Skin skin,
            final BuildPlacementSystem buildSystemToUse,
            final GraphicsSettings graphics
    ) {
        this.buildSystem = buildSystemToUse;
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
            String costText = def.cost().wood() + "/" + def.cost().stone() + "/" + def.cost().food();
            Label label = new Label(def.label() + " " + costText, skin);
            content.add(label);
            button.add(content).pad(PAD);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    buildSystem.setSelectedBuilding(def.id());
                }
            });
            group.add(button);
            String current = null;
            try {
                current = buildSystem.getSelectedBuilding();
            } catch (NullPointerException ignore) {
                // system not initialized
            }
            if (current != null && def.id().equalsIgnoreCase(current)) {
                button.setChecked(true);
            }
            add(button).pad(PAD).row();
        }
        // no further selection
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
    }
}
