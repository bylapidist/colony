package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.client.ui.ChatBox;

/**
 * Container for UI elements of the map screen.
 */
public final class MapUi {

    private final Stage stage;
    private final MinimapActor minimapActor;
    private final ChatBox chatBox;

    public MapUi(final Stage uiStage, final MinimapActor minimap, final ChatBox chat) {
        this.stage = uiStage;
        this.minimapActor = minimap;
        this.chatBox = chat;
    }

    public Stage getStage() {
        return stage;
    }

    public MinimapActor getMinimapActor() {
        return minimapActor;
    }

    public ChatBox getChatBox() {
        return chatBox;
    }
}
