package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.client.systems.input.BuildingRemovalHandler;
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

    public BuildPlacementSystem(final GameClient clientToUse, final KeyBindings bindings) {
        this.client = clientToUse;
        this.keyBindings = bindings;
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
    }

    public boolean isBuildMode() {
        return buildMode;
    }

    public void setRemoveMode(final boolean mode) {
        this.removeMode = mode;
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
