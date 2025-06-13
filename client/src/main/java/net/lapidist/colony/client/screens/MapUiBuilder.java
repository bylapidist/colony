package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.artemis.World;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.client.ui.ChatBox;
import net.lapidist.colony.client.ui.PlayerResourcesActor;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.KeyAction;

/**
 * Builds the UI for {@link MapScreen}.
 */
public final class MapUiBuilder {

    private static final float PADDING = 10f;
    private static final int COLUMN_SPAN = 3;

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
            final GameClient client,
            final Colony colony
    ) {
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        KeyBindings keyBindings = colony.getSettings().getKeyBindings();

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        stage.addActor(table);

        Table chatTable = new Table();
        chatTable.setFillParent(true);
        chatTable.bottom().left();
        stage.addActor(chatTable);

        TextButton menuButton = new TextButton(I18n.get("map.menu"), skin);
        menuButton.setName("menuButton");
        TextButton buildButton = new TextButton(I18n.get("map.build"), skin);
        buildButton.setName("buildButton");
        TextButton removeButton = new TextButton(I18n.get("map.remove"), skin);
        removeButton.setName("removeButton");
        TextButton mapButton = new TextButton(I18n.get("map.map"), skin);
        mapButton.setName("mapButton");
        TextButton minimapButton = new TextButton(I18n.get("map.minimap"), skin);
        minimapButton.setName("minimapButton");
        GraphicsSettings graphics = colony.getSettings().getGraphicsSettings();
        MinimapActor minimapActor = new MinimapActor(world, graphics);
        ChatBox chatBox = new ChatBox(skin, client);
        PlayerResourcesActor resourcesActor = new PlayerResourcesActor(skin, world);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.returnToMainMenu();
            }
        });

        BuildPlacementSystem buildSystem = world.getSystem(BuildPlacementSystem.class);
        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);

        buildButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                boolean enabled = !buildSystem.isBuildMode();
                buildSystem.setBuildMode(enabled);
                if (enabled) {
                    buildSystem.setRemoveMode(false);
                }
            }
        });

        removeButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                boolean enabled = !buildSystem.isRemoveMode();
                buildSystem.setRemoveMode(enabled);
                if (enabled) {
                    buildSystem.setBuildMode(false);
                }
            }
        });

        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                cameraSystem.toggleMode();
            }
        });

        minimapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                minimapActor.setVisible(!minimapActor.isVisible());
            }
        });

        table.add(menuButton).pad(PADDING).left().top();
        table.add(buildButton).pad(PADDING).left().top();
        table.add(removeButton).pad(PADDING).left().top();
        table.add(mapButton).pad(PADDING).left().top();
        table.add(minimapButton).pad(PADDING).left().top();
        table.add(resourcesActor).pad(PADDING).expandX().left().top();
        table.add(minimapActor).pad(PADDING).right().top();
        table.row();
        chatTable.add(chatBox).pad(PADDING).growX();

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == keyBindings.getKey(KeyAction.CHAT) && !chatBox.isInputVisible()) {
                    chatBox.showInput();
                    return true;
                }
                if (keycode == keyBindings.getKey(KeyAction.MINIMAP)) {
                    minimapActor.setVisible(!minimapActor.isVisible());
                    return true;
                }
                return false;
            }
        });
        return new MapUi(stage, minimapActor, chatBox);
    }
}
