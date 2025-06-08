package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.ui.MinimapActor;

/**
 * Container for UI elements of the map screen.
 */
public final class MapUi {

    private final Stage stage;
    private final MinimapActor minimapActor;

    public MapUi(final Stage uiStage, final MinimapActor minimap) {
        this.stage = uiStage;
        this.minimapActor = minimap;
    }

    public Stage getStage() {
        return stage;
    }

    public MinimapActor getMinimapActor() {
        return minimapActor;
    }
}
