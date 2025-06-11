package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.artemis.Entity;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.GestureInputHandler;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.client.systems.input.InputGestureListener;
import net.lapidist.colony.client.systems.input.ScrollInputProcessor;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.map.MapUtils;
import com.artemis.ComponentMapper;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

public final class InputSystem extends BaseSystem {

    private PlayerCameraSystem cameraSystem;

    private final GameClient client;

    private final InputMultiplexer multiplexer = new InputMultiplexer();

    private final Array<Entity> selectedTiles = new Array<>();

    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<net.lapidist.colony.components.resources.ResourceComponent> resourceMapper;

    private final KeyBindings keyBindings;

    private KeyboardInputHandler keyboardHandler;
    private GestureInputHandler gestureHandler;
    private TileSelectionHandler tileSelectionHandler;
    private BuildingPlacementHandler buildingPlacementHandler;
    private InputGestureListener gestureListener;
    private boolean buildMode;

    public InputSystem(final GameClient clientToSet, final KeyBindings bindings) {
        this.client = clientToSet;
        this.keyBindings = bindings;
    }

    public void addProcessor(final InputProcessor processor) {
        multiplexer.addProcessor(processor);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        resourceMapper = world.getMapper(net.lapidist.colony.components.resources.ResourceComponent.class);
        map = net.lapidist.colony.map.MapUtils.findMap(world).orElse(null);
        if (map != null) {
            for (int i = 0; i < map.getTiles().size; i++) {
                var tile = map.getTiles().get(i);
                if (tileMapper.get(tile).isSelected()) {
                    selectedTiles.add(tile);
                }
            }
        }
        keyboardHandler = new KeyboardInputHandler(cameraSystem, keyBindings);
        gestureHandler = new GestureInputHandler(cameraSystem);
        tileSelectionHandler = new TileSelectionHandler(client, cameraSystem, selectedTiles);
        buildingPlacementHandler = new BuildingPlacementHandler(client, cameraSystem);
        gestureListener = new InputGestureListener(
                gestureHandler,
                keyboardHandler,
                tileSelectionHandler,
                map,
                tileMapper
        );
        InputProcessor scrollProcessor = new ScrollInputProcessor(gestureHandler);
        multiplexer.addProcessor(new GestureDetector(gestureListener));
        multiplexer.addProcessor(scrollProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = net.lapidist.colony.map.MapUtils.findMap(world).orElse(null);
            if (map != null) {
                gestureListener.setMap(map);
                for (int i = 0; i < map.getTiles().size; i++) {
                    var tile = map.getTiles().get(i);
                    if (tileMapper.get(tile).isSelected()) {
                        selectedTiles.add(tile);
                    }
                }
            }
        }
        keyboardHandler.handleKeyboardInput(world.getDelta());
        keyboardHandler.clampCameraPosition();
        cameraSystem.getCamera().update();

        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.GATHER)) && map != null) {
            for (int i = 0; i < selectedTiles.size; i++) {
                var tile = selectedTiles.get(i);
                TileComponent tc = tileMapper.get(tile);
                var rc = resourceMapper.get(tile);
                ResourceType type = getResourceType(rc);
                if (type != null) {
                    ResourceGatherRequestData msg = new ResourceGatherRequestData(
                            tc.getX(), tc.getY(), type.name());
                    client.sendGatherRequest(msg);
                }
            }
        }
    }


    public boolean scrolled(final float amountX, final float amountY) {
        return gestureHandler.scrolled(amountX, amountY);
    }

    public boolean tap(final float x, final float y, final int count, final int button) {
        if (buildMode) {
            return buildingPlacementHandler.handleTap(x, y, map, tileMapper);
        }
        boolean result = tileSelectionHandler.handleTap(x, y, map, tileMapper);
        if (map != null) {
            cameraSystem.getCamera().update();
            Vector2 worldCoords = CameraUtils.screenToWorldCoords(
                    cameraSystem.getViewport(), x, y);
            Vector2 tileCoords = CameraUtils.worldCoordsToTileCoords(worldCoords);
            MapUtils.findTile(map, (int) tileCoords.x, (int) tileCoords.y, tileMapper)
                    .ifPresent(tile -> {
                        TileComponent tc = tileMapper.get(tile);
                        var rc = resourceMapper.get(tile);
                        ResourceType type = getResourceType(rc);
                        if (type != null) {
                            ResourceGatherRequestData msg = new ResourceGatherRequestData(
                                    tc.getX(), tc.getY(), type.name());
                            client.sendGatherRequest(msg);
                        }
                    });
        }
        return result;
    }

    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        boolean result = gestureHandler.pan(deltaX, deltaY);
        keyboardHandler.clampCameraPosition();
        return result;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        return gestureHandler.zoom(initialDistance, distance);
    }

    private ResourceType getResourceType(final net.lapidist.colony.components.resources.ResourceComponent rc) {
        if (rc.getWood() > 0) {
            return ResourceType.WOOD;
        } else if (rc.getStone() > 0) {
            return ResourceType.STONE;
        } else if (rc.getFood() > 0) {
            return ResourceType.FOOD;
        }
        return null;
    }

    public void setBuildMode(final boolean mode) {
        this.buildMode = mode;
    }
}
