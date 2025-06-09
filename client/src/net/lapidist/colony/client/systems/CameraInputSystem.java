package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import net.lapidist.colony.client.systems.input.GestureInputHandler;
import net.lapidist.colony.client.systems.input.KeyboardInputHandler;
import net.lapidist.colony.client.systems.input.ScrollInputProcessor;
import net.lapidist.colony.settings.KeyBindings;

/**
 * Handles camera movement via gestures and keyboard input.
 */
public final class CameraInputSystem extends BaseSystem {

    private final KeyBindings keyBindings;
    private final InputMultiplexer multiplexer = new InputMultiplexer();

    private PlayerCameraSystem cameraSystem;
    private KeyboardInputHandler keyboardHandler;
    private GestureInputHandler gestureHandler;

    public CameraInputSystem(final KeyBindings bindings) {
        this.keyBindings = bindings;
    }

    public void addProcessor(final InputProcessor processor) {
        multiplexer.addProcessor(0, processor);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        keyboardHandler = new KeyboardInputHandler(cameraSystem, keyBindings);
        gestureHandler = new GestureInputHandler(cameraSystem);

        multiplexer.addProcessor(new GestureDetector(new CameraGestureListener()));
        multiplexer.addProcessor(new ScrollInputProcessor(gestureHandler));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    protected void processSystem() {
        keyboardHandler.handleKeyboardInput(world.getDelta());
        keyboardHandler.clampCameraPosition();
        cameraSystem.getCamera().update();
    }

    public boolean scrolled(final float amountX, final float amountY) {
        return gestureHandler.scrolled(amountX, amountY);
    }

    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        boolean result = gestureHandler.pan(deltaX, deltaY);
        keyboardHandler.clampCameraPosition();
        return result;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        return gestureHandler.zoom(initialDistance, distance);
    }

    private final class CameraGestureListener extends GestureAdapter {
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
