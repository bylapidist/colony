package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;
import com.artemis.Entity;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

/**
 * Handles tile selection and gather commands.
 */
public final class SelectionSystem extends BaseSystem {

    private final GameClient client;
    private final KeyBindings keyBindings;

    private boolean selectMode;

    private final Array<Entity> selectedTiles = new Array<>();

    private PlayerCameraSystem cameraSystem;
    private CameraInputSystem cameraInputSystem;

    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<ResourceComponent> resourceMapper;

    private TileSelectionHandler tileSelectionHandler;

    private String resourceId = "WOOD";

    public SelectionSystem(final GameClient clientToUse, final KeyBindings bindings) {
        this.client = clientToUse;
        this.keyBindings = bindings;
    }

    /** Enable or disable selection mode. */
    public void setSelectMode(final boolean mode) {
        this.selectMode = mode;
    }

    /** @return whether selection mode is currently enabled. */
    public boolean isSelectMode() {
        return selectMode;
    }

    @Override
    public void initialize() {
        selectMode = false;
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        cameraInputSystem = world.getSystem(CameraInputSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        resourceMapper = world.getMapper(ResourceComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        if (map != null) {
            for (int i = 0; i < map.getTiles().size; i++) {
                var tile = map.getTiles().get(i);
                if (tileMapper.get(tile).isSelected()) {
                    selectedTiles.add(tile);
                }
            }
        }
        tileSelectionHandler = new TileSelectionHandler(client, cameraSystem, selectedTiles);
        GestureDetector detector = new GestureDetector(new SelectionGestureListener());
        cameraInputSystem.addProcessor(detector);
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
            if (map != null) {
                for (int i = 0; i < map.getTiles().size; i++) {
                    var tile = map.getTiles().get(i);
                    if (tileMapper.get(tile).isSelected()) {
                        selectedTiles.add(tile);
                    }
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.SELECT))) {
            selectMode = !selectMode;
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.SELECT_WOOD))) {
            resourceId = "WOOD";
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.SELECT_STONE))) {
            resourceId = "STONE";
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.SELECT_FOOD))) {
            resourceId = "FOOD";
        }
        if (map != null && Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.GATHER))) {
            for (int i = 0; i < selectedTiles.size; i++) {
                var tile = selectedTiles.get(i);
                TileComponent tc = tileMapper.get(tile);
                ResourceComponent rc = resourceMapper.get(tile);
                if (rc.getAmount(resourceId) > 0) {
                    ResourceGatherRequestData msg = new ResourceGatherRequestData(
                            tc.getX(), tc.getY(), resourceId);
                    client.sendGatherRequest(msg);
                }
            }
        }
    }

    public boolean tap(final float x, final float y) {
        if (!selectMode) {
            return false;
        }
        if (map == null) {
            return false;
        }
        cameraSystem.getCamera().update();
        return handleTap(x, y);
    }

    private boolean handleTap(final float x, final float y) {
        if (!selectMode) {
            return false;
        }
        boolean result = tileSelectionHandler.handleTap(x, y, map, tileMapper);
        var worldCoords = CameraUtils.screenToWorldCoords(cameraSystem.getViewport(), x, y);
        var tileCoords = CameraUtils.worldCoordsToTileCoords(worldCoords);
        MapUtils.findTile(map, (int) tileCoords.x, (int) tileCoords.y)
                .ifPresent(tile -> {
                    TileComponent tc = tileMapper.get(tile);
                    ResourceComponent rc = resourceMapper.get(tile);
                    if (rc.getAmount(resourceId) > 0) {
                        ResourceGatherRequestData msg = new ResourceGatherRequestData(
                                tc.getX(), tc.getY(), resourceId);
                        client.sendGatherRequest(msg);
                    }
                });
        return result;
    }

    private final class SelectionGestureListener extends GestureDetector.GestureAdapter {
        @Override
        public boolean tap(final float x, final float y, final int count, final int button) {
            return SelectionSystem.this.tap(x, y);
        }
    }
}
