package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TileSelectionData;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;

public final class InputSystem extends BaseSystem implements InputProcessor, GestureListener {

    private static final float CAMERA_SPEED = 400f; // units per second
    private static final float ZOOM_SPEED = 0.02f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2f;

    private PlayerCameraSystem cameraSystem;

    private final GameClient client;

    private Entity map;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;

    public InputSystem(final GameClient clientToSet) {
        this.client = clientToSet;
        final InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        mapMapper = world.getMapper(MapComponent.class);
        tileMapper = world.getMapper(TileComponent.class);
        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        if (maps.size() > 0) {
            map = world.getEntity(maps.get(0));
        }
    }

    @Override
    protected void processSystem() {
        handleKeyboardInput(world.getDelta());
        clampCameraPosition();
        cameraSystem.getCamera().update();
    }

    private void handleKeyboardInput(final float deltaTime) {
        final float moveAmount = CAMERA_SPEED * deltaTime;
        final Vector3 position = cameraSystem.getCamera().position;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += moveAmount;
        }
    }

    private void clampCameraPosition() {
        final Vector3 position = cameraSystem.getCamera().position;
        final float mapWidth = Constants.MAP_WIDTH * Constants.TILE_SIZE;
        final float mapHeight = Constants.MAP_HEIGHT * Constants.TILE_SIZE;

        position.x = MathUtils.clamp(position.x, 0, mapWidth);
        position.y = MathUtils.clamp(position.y, 0, mapHeight);
    }

    @Override
    public boolean scrolled(final float amountX, final float amountY) {
        final float zoom = cameraSystem.getCamera().zoom + amountY * ZOOM_SPEED;
        cameraSystem.getCamera().zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
        return true;
    }

    @Override
    public boolean touchDown(final float x, final float y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean tap(final float x, final float y, final int count, final int button) {
        if (map == null) {
            return false;
        }

        Vector2 worldCoords = cameraSystem.screenCoordsToWorldCoords(x, y);
        Vector2 tileCoords = cameraSystem.worldCoordsToTileCoords(worldCoords);

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tileComponent = tileMapper.get(tile);
            if (tileComponent.getX() == (int) tileCoords.x && tileComponent.getY() == (int) tileCoords.y) {
                boolean newState = !tileComponent.isSelected();
                tileComponent.setSelected(newState);

                TileSelectionData msg = new TileSelectionData();
                msg.setX(tileComponent.getX());
                msg.setY(tileComponent.getY());
                msg.setSelected(newState);
                client.sendTileSelection(msg);
                return true;
            }
        }

        return false;
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
        cameraSystem.getCamera().translate(
                -deltaX * cameraSystem.getCamera().zoom,
                deltaY * cameraSystem.getCamera().zoom,
                0
        );
        clampCameraPosition();
        return true;
    }

    @Override
    public boolean panStop(final float x, final float y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean zoom(final float initialDistance, final float distance) {
        final float ratio = initialDistance / distance;
        final float zoom = cameraSystem.getCamera().zoom * ratio;
        cameraSystem.getCamera().zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
        return true;
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
