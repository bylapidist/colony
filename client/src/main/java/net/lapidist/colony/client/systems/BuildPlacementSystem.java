package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
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

    private PlayerCameraSystem cameraSystem;
    private CameraInputSystem cameraInputSystem;
    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private BuildingPlacementHandler buildingPlacementHandler;

    public BuildPlacementSystem(final GameClient clientToUse, final KeyBindings bindings) {
        this.client = clientToUse;
        this.keyBindings = bindings;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        cameraInputSystem = world.getSystem(CameraInputSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        buildingPlacementHandler = new BuildingPlacementHandler(client, cameraSystem);
        cameraInputSystem.addProcessor(0, new GestureDetector(new BuildGestureListener()));
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.BUILD))) {
            buildMode = !buildMode;
        }
    }

    public boolean tap(final float x, final float y) {
        if (!buildMode) {
            return false;
        }
        return buildingPlacementHandler.handleTap(x, y, map, tileMapper);
    }

    public void setBuildMode(final boolean mode) {
        this.buildMode = mode;
    }

    public boolean isBuildMode() {
        return buildMode;
    }

    private final class BuildGestureListener extends GestureDetector.GestureAdapter {
        @Override
        public boolean tap(final float x, final float y, final int count, final int button) {
            return BuildPlacementSystem.this.tap(x, y);
        }
    }
}
