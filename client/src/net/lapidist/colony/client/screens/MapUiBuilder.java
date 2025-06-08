package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.artemis.World;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.client.ui.ChatLogActor;
import net.lapidist.colony.client.network.GameClient;
import com.mojang.brigadier.CommandDispatcher;
import net.lapidist.colony.i18n.I18n;

/**
 * Builds the UI for {@link MapScreen}.
 */
public final class MapUiBuilder {

    private static final float PADDING = 10f;

    private MapUiBuilder() {
    }

    /**
     * Setup UI elements for the map screen.
     *
     * @param stage  stage to attach UI to
     * @param world  game world for rendering information
     * @param colony owning game instance
     * @return container with built UI objects
     */
    public static MapUi build(
            final Stage stage,
            final World world,
            final Colony colony,
            final GameClient client
    ) {
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        stage.addActor(table);

        TextButton menuButton = new TextButton(I18n.get("map.menu"), skin);
        MinimapActor minimapActor = new MinimapActor(world);
        CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();
        ChatLogActor chatLogActor = new ChatLogActor(client, skin, dispatcher);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.returnToMainMenu();
            }
        });

        table.add(menuButton).pad(PADDING).left().top();
        table.add(minimapActor).pad(PADDING).expandX().right().top();
        table.row();
        table.add(chatLogActor).pad(PADDING).colspan(2).left().bottom().growX();

        return new MapUi(stage, minimapActor, chatLogActor);
    }
}
