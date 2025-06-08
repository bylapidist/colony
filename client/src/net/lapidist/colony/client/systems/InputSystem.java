package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.CameraInputHandler;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

public final class InputSystem extends BaseSystem implements InputProcessor, GestureListener {

    private PlayerCameraSystem cameraSystem;

    private final GameClient client;

    private final InputMultiplexer multiplexer = new InputMultiplexer();

    private Entity map;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;

    private CameraInputHandler cameraHandler;
    private TileSelectionHandler tileSelectionHandler;

    public InputSystem(final GameClient clientToSet) {
        this.client = clientToSet;
        multiplexer.addProcessor(new GestureDetector(this));
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void addProcessor(final InputProcessor processor) {
        multiplexer.addProcessor(0, processor);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        mapMapper = world.getMapper(MapComponent.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = net.lapidist.colony.map.MapUtils.findMapEntity(world);
        cameraHandler = new CameraInputHandler(cameraSystem);
        tileSelectionHandler = new TileSelectionHandler(client, cameraSystem);
    }

    @Override
    protected void processSystem() {
        cameraHandler.handleKeyboardInput(world.getDelta());
        cameraHandler.clampCameraPosition();
        cameraSystem.getCamera().update();
    }


    @Override
    public boolean scrolled(final float amountX, final float amountY) {
        return cameraHandler.scrolled(amountX, amountY);
    }

    @Override
    public boolean touchDown(final float x, final float y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean tap(final float x, final float y, final int count, final int button) {
        return tileSelectionHandler.handleTap(x, y, map, mapMapper, tileMapper);
    }

    @Override
    public boolean longPress(final float x, final float y) {
        return false;
    }

    @Override
    public boolean fling(final float velocityX, final float velocityY, final int button) {
        return false;
    }

    @Override
    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        return cameraHandler.pan(deltaX, deltaY);
    }

    @Override
    public boolean panStop(final float x, final float y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean zoom(final float initialDistance, final float distance) {
        return cameraHandler.zoom(initialDistance, distance);
    }

    @Override
    public boolean pinch(
            final Vector2 initialPointer1,
            final Vector2 initialPointer2,
            final Vector2 pointer1,
            final Vector2 pointer2
    ) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(final int i, final int i1, final int i2, final int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }
}
