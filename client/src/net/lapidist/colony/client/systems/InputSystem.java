package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.GestureInputHandler;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.client.systems.input.InputGestureListener;
import net.lapidist.colony.client.systems.input.ScrollInputProcessor;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import com.artemis.ComponentMapper;

public final class InputSystem extends BaseSystem {

    private PlayerCameraSystem cameraSystem;

    private final GameClient client;

    private final InputMultiplexer multiplexer = new InputMultiplexer();

    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;

    private KeyboardInputHandler keyboardHandler;
    private GestureInputHandler gestureHandler;
    private TileSelectionHandler tileSelectionHandler;
    private InputGestureListener gestureListener;

    public InputSystem(final GameClient clientToSet) {
        this.client = clientToSet;
    }

    public void addProcessor(final InputProcessor processor) {
        multiplexer.addProcessor(0, processor);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = net.lapidist.colony.map.MapUtils.findMap(world).orElse(null);
        keyboardHandler = new KeyboardInputHandler(cameraSystem);
        gestureHandler = new GestureInputHandler(cameraSystem);
        tileSelectionHandler = new TileSelectionHandler(client, cameraSystem);
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
            }
        }
        keyboardHandler.handleKeyboardInput(world.getDelta());
        keyboardHandler.clampCameraPosition();
        cameraSystem.getCamera().update();
    }


    public boolean scrolled(final float amountX, final float amountY) {
        return gestureHandler.scrolled(amountX, amountY);
    }

    public boolean tap(final float x, final float y, final int count, final int button) {
        return tileSelectionHandler.handleTap(x, y, map, tileMapper);
    }

    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        boolean result = gestureHandler.pan(deltaX, deltaY);
        keyboardHandler.clampCameraPosition();
        return result;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        return gestureHandler.zoom(initialDistance, distance);
    }
}
