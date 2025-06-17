package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import net.lapidist.colony.client.ui.BuildMenuActor;
import net.lapidist.colony.client.ui.AutosaveLabel;
import net.lapidist.colony.client.ui.AutosaveProgressBar;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.util.I18n;
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
        TextButton buildButton = new TextButton("", skin, "toggle");
        buildButton.setName("buildButton");
        TextButton removeButton = new TextButton("", skin, "toggle");
        removeButton.setName("removeButton");
        TextButton mapButton = new TextButton("", skin, "toggle");
        mapButton.setName("mapButton");
        TextButton minimapButton = new TextButton("", skin, "toggle");
        minimapButton.setName("minimapButton");
        GraphicsSettings graphics = colony.getSettings().getGraphicsSettings();
        MinimapActor minimapActor = new MinimapActor(world, graphics, client);
        minimapButton.setChecked(minimapActor.isVisible());
        ChatBox chatBox = new ChatBox(skin, client);
        PlayerResourcesActor resourcesActor = new PlayerResourcesActor(skin, world);
        BuildPlacementSystem buildSystem = world.getSystem(BuildPlacementSystem.class);
        BuildMenuActor buildMenu = null;
        if (buildSystem != null) {
            buildMenu = new BuildMenuActor(skin, buildSystem, graphics);
            stage.addActor(buildMenu);
        }

        setButtonText(buildButton, "map.build", keyBindings.getKey(KeyAction.BUILD));
        setButtonText(removeButton, "map.remove", keyBindings.getKey(KeyAction.REMOVE));
        setButtonText(mapButton, "map.map", keyBindings.getKey(KeyAction.TOGGLE_CAMERA));
        setButtonText(minimapButton, "map.minimap", keyBindings.getKey(KeyAction.MINIMAP));

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.returnToMainMenu();
            }
        });

        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        if (cameraSystem != null) {
            mapButton.setChecked(cameraSystem.getMode() == PlayerCameraSystem.Mode.MAP_OVERVIEW);
        }

        buildButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                boolean enabled = buildButton.isChecked();
                buildSystem.setBuildMode(enabled);
                if (enabled) {
                    removeButton.setProgrammaticChangeEvents(false);
                    removeButton.setChecked(false);
                    removeButton.setProgrammaticChangeEvents(true);
                }
            }
        });

        removeButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                boolean enabled = removeButton.isChecked();
                buildSystem.setRemoveMode(enabled);
                if (enabled) {
                    buildButton.setProgrammaticChangeEvents(false);
                    buildButton.setChecked(false);
                    buildButton.setProgrammaticChangeEvents(true);
                }
            }
        });

        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                if (cameraSystem != null) {
                    cameraSystem.toggleMode();
                }
            }
        });

        minimapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                minimapActor.setVisible(minimapButton.isChecked());
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

        stage.addActor(new ShortcutUpdater(keyBindings, buildButton, removeButton, mapButton, minimapButton));
        stage.addActor(new ModeUpdater(buildSystem, buildButton, removeButton, buildMenu));
        if (cameraSystem != null) {
            stage.addActor(new CameraModeUpdater(cameraSystem, mapButton));
        }

        AutosaveLabel savingLabel = new AutosaveLabel(skin);
        Table savingTable = new Table();
        savingTable.setFillParent(true);
        savingTable.top().right();
        savingTable.add(savingLabel).pad(PADDING);
        stage.addActor(savingTable);

        AutosaveProgressBar progressBar = new AutosaveProgressBar(skin);
        Table progressTable = new Table();
        progressTable.setFillParent(true);
        progressTable.bottom().right();
        progressTable.add(progressBar).pad(PADDING);
        stage.addActor(progressTable);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == keyBindings.getKey(KeyAction.CHAT) && !chatBox.isInputVisible()) {
                    chatBox.showInput();
                    return true;
                }
                if (keycode == keyBindings.getKey(KeyAction.MINIMAP)) {
                    boolean visible = !minimapActor.isVisible();
                    minimapActor.setVisible(visible);
                    minimapButton.setProgrammaticChangeEvents(false);
                    minimapButton.setChecked(visible);
                    minimapButton.setProgrammaticChangeEvents(true);
                    return true;
                }
                return false;
            }
        });
        return new MapUi(stage, minimapActor, chatBox);
    }

    private static void setButtonText(final TextButton button, final String key, final int code) {
        button.setText(I18n.get(key) + " [" + Input.Keys.toString(code) + "]");
    }

    private static final class ShortcutUpdater extends Actor {
        private final KeyBindings bindings;
        private final TextButton build;
        private final TextButton remove;
        private final TextButton map;
        private final TextButton minimap;
        private int buildKey;
        private int removeKey;
        private int mapKey;
        private int minimapKey;

        ShortcutUpdater(
                final KeyBindings bindingsToUse,
                final TextButton buildButton,
                final TextButton removeButton,
                final TextButton mapButton,
                final TextButton minimapButton
        ) {
            this.bindings = bindingsToUse;
            this.build = buildButton;
            this.remove = removeButton;
            this.map = mapButton;
            this.minimap = minimapButton;
            refresh();
        }

        @Override
        public void act(final float delta) {
            super.act(delta);
            if (buildKey != bindings.getKey(KeyAction.BUILD)
                    || removeKey != bindings.getKey(KeyAction.REMOVE)
                    || mapKey != bindings.getKey(KeyAction.TOGGLE_CAMERA)
                    || minimapKey != bindings.getKey(KeyAction.MINIMAP)) {
                refresh();
            }
        }

        private void refresh() {
            buildKey = bindings.getKey(KeyAction.BUILD);
            removeKey = bindings.getKey(KeyAction.REMOVE);
            mapKey = bindings.getKey(KeyAction.TOGGLE_CAMERA);
            minimapKey = bindings.getKey(KeyAction.MINIMAP);
            setButtonText(build, "map.build", buildKey);
            setButtonText(remove, "map.remove", removeKey);
            setButtonText(map, "map.map", mapKey);
            setButtonText(minimap, "map.minimap", minimapKey);
        }
    }

    private static final class CameraModeUpdater extends Actor {
        private final PlayerCameraSystem cameraSystem;
        private final TextButton mapButton;

        CameraModeUpdater(
                final PlayerCameraSystem systemToUse,
                final TextButton buttonToUse
        ) {
            this.cameraSystem = systemToUse;
            this.mapButton = buttonToUse;
        }

        @Override
        public void act(final float delta) {
            super.act(delta);
            boolean overview = cameraSystem.getMode() == PlayerCameraSystem.Mode.MAP_OVERVIEW;
            if (mapButton.isChecked() != overview) {
                mapButton.setProgrammaticChangeEvents(false);
                mapButton.setChecked(overview);
                mapButton.setProgrammaticChangeEvents(true);
            }
        }
    }

    private static final class ModeUpdater extends Actor {
        private final BuildPlacementSystem buildSystem;
        private final TextButton buildButton;
        private final TextButton removeButton;
        private final Actor buildMenu;

        ModeUpdater(
                final BuildPlacementSystem buildSystemToUse,
                final TextButton buildBtn,
                final TextButton removeBtn,
                final Actor menu
        ) {
            this.buildSystem = buildSystemToUse;
            this.buildButton = buildBtn;
            this.removeButton = removeBtn;
            this.buildMenu = menu;
        }

        @Override
        public void act(final float delta) {
            super.act(delta);
            boolean build = buildSystem.isBuildMode();
            boolean remove = buildSystem.isRemoveMode();
            if (buildButton.isChecked() != build) {
                buildButton.setProgrammaticChangeEvents(false);
                buildButton.setChecked(build);
                buildButton.setProgrammaticChangeEvents(true);
            }
            if (removeButton.isChecked() != remove) {
                removeButton.setProgrammaticChangeEvents(false);
                removeButton.setChecked(remove);
                removeButton.setProgrammaticChangeEvents(true);
            }
            if (buildMenu != null) {
                buildMenu.setVisible(build);
            }
        }
    }
}
