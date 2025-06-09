package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

/**
 * Processes tile selection and gather commands.
 */
public final class SelectionSystem extends BaseSystem {

    private final GameClient client;
    private final KeyBindings keyBindings;

    private PlayerCameraSystem cameraSystem;
    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<ResourceComponent> resourceMapper;
    private TileSelectionHandler selectionHandler;

    public SelectionSystem(final GameClient clientToSet, final KeyBindings bindings) {
        this.client = clientToSet;
        this.keyBindings = bindings;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        resourceMapper = world.getMapper(ResourceComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        selectionHandler = new TileSelectionHandler(client, cameraSystem);
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
        }
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.GATHER)) && map != null) {
            for (int i = 0; i < map.getTiles().size; i++) {
                var tile = map.getTiles().get(i);
                TileComponent tc = tileMapper.get(tile);
                if (tc.isSelected()) {
                    ResourceGatherRequestData msg = new ResourceGatherRequestData(
                            tc.getX(), tc.getY(), ResourceType.WOOD.name());
                    client.sendGatherRequest(msg);
                }
            }
        }
    }

    public boolean tap(final float x, final float y, final int count, final int button) {
        boolean result = selectionHandler.handleTap(x, y, map, tileMapper);
        if (map != null) {
            cameraSystem.getCamera().update();
            Vector2 worldCoords = CameraUtils.screenToWorldCoords(cameraSystem.getViewport(), x, y);
            Vector2 tileCoords = CameraUtils.worldCoordsToTileCoords(worldCoords);
            MapUtils.findTile(map, (int) tileCoords.x, (int) tileCoords.y, tileMapper)
                    .ifPresent(tile -> {
                        TileComponent tc = tileMapper.get(tile);
                        var rc = resourceMapper.get(tile);
                        if (rc.getWood() > 0) {
                            ResourceGatherRequestData msg = new ResourceGatherRequestData(
                                    tc.getX(), tc.getY(), ResourceType.WOOD.name());
                            client.sendGatherRequest(msg);
                        }
                    });
        }
        return result;
    }
}
