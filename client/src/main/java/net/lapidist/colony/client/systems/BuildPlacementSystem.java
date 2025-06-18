package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.client.systems.input.BuildingRemovalHandler;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.BuildingDefinition;
import com.badlogic.gdx.Input;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

/**
 * Handles building placement when build mode is enabled.
 */
public final class BuildPlacementSystem extends BaseSystem {

    private static final int[] NUMBER_KEYS = {
            Input.Keys.NUM_1,
            Input.Keys.NUM_2,
            Input.Keys.NUM_3,
            Input.Keys.NUM_4,
            Input.Keys.NUM_5,
            Input.Keys.NUM_6,
            Input.Keys.NUM_7,
            Input.Keys.NUM_8,
            Input.Keys.NUM_9
    };

    private final GameClient client;
    private final KeyBindings keyBindings;
    private boolean buildMode;
    private boolean removeMode;

    private PlayerCameraSystem cameraSystem;
    private CameraInputSystem cameraInputSystem;
    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<BuildingComponent> buildingMapper;
    private BuildingPlacementHandler buildingPlacementHandler;
    private BuildingRemovalHandler buildingRemovalHandler;
    private java.util.List<String> buildingIds;
    private int selectedIndex;

    public BuildPlacementSystem(final GameClient clientToUse, final KeyBindings bindings) {
        this.client = clientToUse;
        this.keyBindings = bindings;
    }

    /** Select building by list index and update placement handler. */
    private void selectBuilding(final int index) {
        if (index < 0 || index >= buildingIds.size()) {
            return;
        }
        selectedIndex = index;
        buildingPlacementHandler.setBuildingId(buildingIds.get(selectedIndex));
    }

    /** Set building id directly if present in registry. */
    public void setSelectedBuilding(final String id) {
        if (id == null) {
            return;
        }
        for (int i = 0; i < buildingIds.size(); i++) {
            if (buildingIds.get(i).equalsIgnoreCase(id)) {
                selectBuilding(i);
                break;
            }
        }
    }

    /** @return currently selected building identifier. */
    public String getSelectedBuilding() {
        if (buildingIds == null || buildingIds.isEmpty()) {
            return null;
        }
        return buildingIds.get(selectedIndex);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        cameraInputSystem = world.getSystem(CameraInputSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        buildingMapper = world.getMapper(BuildingComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        buildingPlacementHandler = new BuildingPlacementHandler(client, cameraSystem);
        buildingRemovalHandler = new BuildingRemovalHandler(client, cameraSystem);
        buildingIds = new java.util.ArrayList<>();
        for (BuildingDefinition def : Registries.buildings().all()) {
            buildingIds.add(def.id());
        }
        selectedIndex = 0;
        if (!buildingIds.isEmpty()) {
            buildingPlacementHandler.setBuildingId(buildingIds.get(selectedIndex));
        }
        cameraInputSystem.addProcessor(1, new GestureDetector(new BuildGestureListener()));
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.BUILD))) {
            buildMode = !buildMode;
            if (buildMode) {
                removeMode = false;
            }
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.REMOVE))) {
            removeMode = !removeMode;
            if (removeMode) {
                buildMode = false;
            }
        }

        // Hotkeys 1-9 to choose building type
        if (buildingIds != null) {
            int max = Math.min(NUMBER_KEYS.length, buildingIds.size());
            for (int i = 0; i < max; i++) {
                if (Gdx.input.isKeyJustPressed(NUMBER_KEYS[i])) {
                    selectBuilding(i);
                }
            }
        }
    }

    public boolean tap(final float x, final float y) {
        if (buildMode) {
            return buildingPlacementHandler.handleTap(x, y, map, tileMapper);
        }
        if (removeMode) {
            return buildingRemovalHandler.handleTap(x, y, map, buildingMapper);
        }
        return false;
    }

    public void setBuildMode(final boolean mode) {
        this.buildMode = mode;
        if (mode) {
            this.removeMode = false;
        }
    }

    public boolean isBuildMode() {
        return buildMode;
    }

    public void setRemoveMode(final boolean mode) {
        this.removeMode = mode;
        if (mode) {
            this.buildMode = false;
        }
    }

    public boolean isRemoveMode() {
        return removeMode;
    }

    private final class BuildGestureListener extends GestureDetector.GestureAdapter {
        @Override
        public boolean tap(final float x, final float y, final int count, final int button) {
            return BuildPlacementSystem.this.tap(x, y);
        }
    }
}
