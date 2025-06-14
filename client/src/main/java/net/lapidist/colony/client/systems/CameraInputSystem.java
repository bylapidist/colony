package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import net.lapidist.colony.client.systems.input.GestureInputHandler;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.client.systems.input.ScrollInputProcessor;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.settings.KeyAction;

/**
 * Handles camera input via gestures and keyboard.
 */
public final class CameraInputSystem extends BaseSystem {

    private final KeyBindings keyBindings;
    private final net.lapidist.colony.client.network.GameClient client;
    private final InputMultiplexer multiplexer = new InputMultiplexer();

    private PlayerCameraSystem cameraSystem;
    private KeyboardInputHandler keyboardHandler;
    private GestureInputHandler gestureHandler;

    public CameraInputSystem(final KeyBindings bindings) {
        this(null, bindings);
    }

    public CameraInputSystem(
            final net.lapidist.colony.client.network.GameClient clientToUse,
            final KeyBindings bindings
    ) {
        this.client = clientToUse;
        this.keyBindings = bindings;
    }

    public void addProcessor(final InputProcessor processor) {
        multiplexer.addProcessor(processor);
    }

    public void addProcessor(final int index, final InputProcessor processor) {
        multiplexer.addProcessor(index, processor);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        keyboardHandler = new KeyboardInputHandler(client, cameraSystem, keyBindings);
        gestureHandler = new GestureInputHandler(cameraSystem);
        GestureDetector detector = new GestureDetector(new CameraGestureListener());
        multiplexer.addProcessor(detector);
        multiplexer.addProcessor(new ScrollInputProcessor(gestureHandler));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    protected void processSystem() {
        if (Gdx.input.isKeyJustPressed(keyBindings.getKey(KeyAction.TOGGLE_CAMERA))) {
            cameraSystem.toggleMode();
        }
        keyboardHandler.handleKeyboardInput(world.getDelta());
        keyboardHandler.clampCameraPosition();
        cameraSystem.getCamera().update();
    }

    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        boolean result = gestureHandler.pan(deltaX, deltaY);
        keyboardHandler.clampCameraPosition();
        return result;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        return gestureHandler.zoom(initialDistance, distance);
    }

    public boolean scrolled(final float amountX, final float amountY) {
        return gestureHandler.scrolled(amountX, amountY);
    }

    private final class CameraGestureListener extends GestureDetector.GestureAdapter {
        @Override
        public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
            return CameraInputSystem.this.pan(x, y, deltaX, deltaY);
        }

        @Override
        public boolean zoom(final float initialDistance, final float distance) {
            return CameraInputSystem.this.zoom(initialDistance, distance);
        }
    }
}
