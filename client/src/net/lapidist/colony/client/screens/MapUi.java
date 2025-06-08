package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.client.ui.ChatLogActor;

/**
 * Container for UI elements of the map screen.
 */
public final class MapUi {

    private final Stage stage;
    private final MinimapActor minimapActor;
    private final ChatLogActor chatLogActor;

    public MapUi(final Stage uiStage, final MinimapActor minimap, final ChatLogActor chat) {
        this.stage = uiStage;
        this.minimapActor = minimap;
        this.chatLogActor = chat;
    }

    public Stage getStage() {
        return stage;
    }

    public MinimapActor getMinimapActor() {
        return minimapActor;
    }

    public ChatLogActor getChatLogActor() {
        return chatLogActor;
    }
}
