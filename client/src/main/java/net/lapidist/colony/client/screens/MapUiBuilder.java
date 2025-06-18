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
import net.lapidist.colony.client.systems.SelectionSystem;
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
        TextButton selectButton = new TextButton("", skin, "toggle");
        selectButton.setName("selectButton");
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
        SelectionSystem selectionSystem = world.getSystem(SelectionSystem.class);
        BuildMenuActor buildMenu = null;
        if (buildSystem != null) {
            buildMenu = new BuildMenuActor(skin, buildSystem, graphics);
            stage.addActor(buildMenu);
        }

        setButtonText(buildButton, "map.build", keyBindings.getKey(KeyAction.BUILD));
        setButtonText(removeButton, "map.remove", keyBindings.getKey(KeyAction.REMOVE));
        setButtonText(selectButton, "map.select", keyBindings.getKey(KeyAction.SELECT));
        setButtonText(mapButton, "map.map", keyBindings.getKey(KeyAction.TOGGLE_CAMERA));
        setButtonText(minimapButton, "map.minimap", keyBindings.getKey(KeyAction.MINIMAP));

        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        if (cameraSystem != null) {
            mapButton.setChecked(cameraSystem.getMode() == PlayerCameraSystem.Mode.MAP_OVERVIEW);
        }

        addMenuListener(colony, menuButton);
        addBuildListener(buildSystem, buildButton, removeButton);
        addRemoveListener(buildSystem, buildButton, removeButton);
        addSelectListener(selectionSystem, selectButton, buildButton, removeButton);
        addMapListener(cameraSystem, mapButton);
        addMinimapListener(minimapButton, minimapActor);

        table.add(menuButton).pad(PADDING).left().top();
        table.add(buildButton).pad(PADDING).left().top();
        table.add(removeButton).pad(PADDING).left().top();
        table.add(selectButton).pad(PADDING).left().top();
        table.add(mapButton).pad(PADDING).left().top();
        table.add(minimapButton).pad(PADDING).left().top();
        table.add(minimapActor).pad(PADDING).expandX().right().top();
        table.row();
        chatTable.add(chatBox).pad(PADDING).growX();

        stage.addActor(new ShortcutUpdater(
                keyBindings,
                buildButton,
                removeButton,
                selectButton,
                mapButton,
                minimapButton
        ));
        stage.addActor(new ModeUpdater(
                buildSystem,
                selectionSystem,
                buildButton,
                removeButton,
                selectButton,
                buildMenu
        ));
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
        Table infoTable = new Table();
        infoTable.setFillParent(true);
        infoTable.bottom().right();
        infoTable.add(resourcesActor).pad(PADDING);
        infoTable.row();
        infoTable.add(progressBar).pad(PADDING);
        stage.addActor(infoTable);

        stage.addListener(createStageListener(keyBindings, chatBox, minimapActor, minimapButton));
        return new MapUi(stage, minimapActor, chatBox);
    }

    private static void addMenuListener(final Colony colony, final TextButton menuButton) {
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.returnToMainMenu();
            }
        });
    }

    private static void addBuildListener(
            final BuildPlacementSystem buildSystem,
            final TextButton buildButton,
            final TextButton removeButton
    ) {
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
    }

    private static void addRemoveListener(
            final BuildPlacementSystem buildSystem,
            final TextButton buildButton,
            final TextButton removeButton
    ) {
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
    }

    private static void addSelectListener(
            final SelectionSystem selectionSystem,
            final TextButton selectButton,
            final TextButton buildButton,
            final TextButton removeButton
    ) {
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                boolean enabled = selectButton.isChecked();
                if (selectionSystem != null) {
                    selectionSystem.setSelectMode(enabled);
                }
                if (enabled) {
                    buildButton.setProgrammaticChangeEvents(false);
                    buildButton.setChecked(false);
                    buildButton.setProgrammaticChangeEvents(true);
                    removeButton.setProgrammaticChangeEvents(false);
                    removeButton.setChecked(false);
                    removeButton.setProgrammaticChangeEvents(true);
                }
            }
        });
    }

    private static void addMapListener(final PlayerCameraSystem cameraSystem, final TextButton mapButton) {
        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                if (cameraSystem != null) {
                    cameraSystem.toggleMode();
                }
            }
        });
    }

    private static void addMinimapListener(final TextButton minimapButton, final MinimapActor minimapActor) {
        minimapButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                minimapActor.setVisible(minimapButton.isChecked());
            }
        });
    }

    private static InputListener createStageListener(
            final KeyBindings keyBindings,
            final ChatBox chatBox,
            final MinimapActor minimapActor,
            final TextButton minimapButton
    ) {
        return new InputListener() {
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
        };
    }

    private static void setButtonText(final TextButton button, final String key, final int code) {
        button.setText(I18n.get(key) + " [" + Input.Keys.toString(code) + "]");
    }

    private static final class ShortcutUpdater extends Actor {
        private final KeyBindings bindings;
        private final TextButton build;
        private final TextButton remove;
        private final TextButton select;
        private final TextButton map;
        private final TextButton minimap;
        private int buildKey;
        private int removeKey;
        private int selectKey;
        private int mapKey;
        private int minimapKey;

        ShortcutUpdater(
                final KeyBindings bindingsToUse,
                final TextButton buildButton,
                final TextButton removeButton,
                final TextButton selectButton,
                final TextButton mapButton,
                final TextButton minimapButton
        ) {
            this.bindings = bindingsToUse;
            this.build = buildButton;
            this.remove = removeButton;
            this.select = selectButton;
            this.map = mapButton;
            this.minimap = minimapButton;
            refresh();
        }

        @Override
        public void act(final float delta) {
            super.act(delta);
            if (buildKey != bindings.getKey(KeyAction.BUILD)
                    || removeKey != bindings.getKey(KeyAction.REMOVE)
                    || selectKey != bindings.getKey(KeyAction.SELECT)
                    || mapKey != bindings.getKey(KeyAction.TOGGLE_CAMERA)
                    || minimapKey != bindings.getKey(KeyAction.MINIMAP)) {
                refresh();
            }
        }

        private void refresh() {
            buildKey = bindings.getKey(KeyAction.BUILD);
            removeKey = bindings.getKey(KeyAction.REMOVE);
            selectKey = bindings.getKey(KeyAction.SELECT);
            mapKey = bindings.getKey(KeyAction.TOGGLE_CAMERA);
            minimapKey = bindings.getKey(KeyAction.MINIMAP);
            setButtonText(build, "map.build", buildKey);
            setButtonText(remove, "map.remove", removeKey);
            setButtonText(select, "map.select", selectKey);
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
        private final SelectionSystem selectionSystem;
        private final TextButton buildButton;
        private final TextButton removeButton;
        private final TextButton selectButton;
        private final Actor buildMenu;

        ModeUpdater(
                final BuildPlacementSystem buildSystemToUse,
                final SelectionSystem selectionSystemToUse,
                final TextButton buildBtn,
                final TextButton removeBtn,
                final TextButton selectBtn,
                final Actor menu
        ) {
            this.buildSystem = buildSystemToUse;
            this.selectionSystem = selectionSystemToUse;
            this.buildButton = buildBtn;
            this.removeButton = removeBtn;
            this.selectButton = selectBtn;
            this.buildMenu = menu;
        }

        @Override
        public void act(final float delta) {
            super.act(delta);
            boolean build = buildSystem.isBuildMode();
            boolean remove = buildSystem.isRemoveMode();
            boolean select = selectionSystem != null && selectionSystem.isSelectMode();
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
            if (selectButton.isChecked() != select) {
                selectButton.setProgrammaticChangeEvents(false);
                selectButton.setChecked(select);
                selectButton.setProgrammaticChangeEvents(true);
            }
            if (buildMenu != null) {
                buildMenu.setVisible(build);
            }
        }
    }
}
