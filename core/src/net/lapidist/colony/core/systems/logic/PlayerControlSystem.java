package net.lapidist.colony.core.systems.logic;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.components.PlayerComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends EntityProcessingSystem implements InputProcessor {

    private final static float MOVEMENT_SPEED = 10f;
    private final static float MIN_ZOOM = 1f;
    private final static float MAX_ZOOM = 2f;
    private final static float ZOOM_SPEED = 0.06f;

    private IntSet downKeys = new IntSet(20);
    private Entity entity;
    private Vector2 position;
    private Vector2 origin;
    private Vector2 tmpVec2;
    private Rectangle mapBounds;
    private CameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerComponent.class));

        tmpVec2 = new Vector2();
        mapBounds = new Rectangle();

        Colony.getInputMultiplexer().addProcessor(this);
    }

    private void processInput() {
        tmpVec2 = tmpVec2.set(position.x, position.y);

        if (singleKeyDown(Input.Keys.W)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x, tmpVec2.y - MOVEMENT_SPEED);
        }

        if (singleKeyDown(Input.Keys.A)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x - MOVEMENT_SPEED, tmpVec2.y);
        }

        if (singleKeyDown(Input.Keys.S)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x, tmpVec2.y + MOVEMENT_SPEED);
        }

        if (singleKeyDown(Input.Keys.D)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x + MOVEMENT_SPEED, tmpVec2.y);
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x + MOVEMENT_SPEED, tmpVec2.y - MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x - MOVEMENT_SPEED, tmpVec2.y - MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x + MOVEMENT_SPEED, tmpVec2.y + MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            tmpVec2 = tmpVec2.set(tmpVec2.x - MOVEMENT_SPEED, tmpVec2.y + MOVEMENT_SPEED);
        }

        if (!isWithinGrid(tmpVec2)) return;

        E(entity)
                .getSpriteComponent().getSprite()
                .setPosition(tmpVec2.x, tmpVec2.y);
    }

    private boolean singleKeyDown(int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    private boolean twoKeysDown(int keycode1, int keycode2) {
        return downKeys.contains(keycode1) && downKeys.contains(keycode2) && downKeys.size == 2;
    }

    private Vector2 getPlayerPosition() {
        Sprite sprite = E(entity).getSpriteComponent().getSprite();

        return new Vector2(
                sprite.getX(),
                sprite.getY()
        );
    }

    private Vector2 getPlayerOrigin() {
        Sprite sprite = E(entity).getSpriteComponent().getSprite();

        return new Vector2(
                sprite.getX() + (sprite.getWidth() / 2),
                sprite.getY() + (sprite.getHeight() / 2)
        );
    }

    private void updatePlayerCell() {
        TiledMapTileLayer layer = mapGenerationSystem.layers.get("unitsLayer");
        int estimatedGridX = (int) (origin.x / mapGenerationSystem.getTileWidth());
        int estimatedGridY = (int) (origin.y / mapGenerationSystem.getTileHeight());

        TiledMapTileLayer.Cell cell = layer.getCell(estimatedGridX, estimatedGridY);

        E(entity).cellComponentCell(cell);
    }

    private boolean isWithinGrid(Vector2 position) {
        return mapBounds.contains(position);
    }

    @Override
    protected void initialize() {
        mapBounds.set(
                0,
                0,
                (mapGenerationSystem.getWidth() * mapGenerationSystem.getTileWidth()) - Constants.PPM,
                (mapGenerationSystem.getHeight() * mapGenerationSystem.getTileHeight()) - Constants.PPM
        );
    }

    @Override
    protected void process(Entity e) {
        entity = e;
        position = getPlayerPosition();
        origin = getPlayerOrigin();

        processInput();
        updatePlayerCell();
    }

    @Override
    public boolean keyDown(int keycode) {
        downKeys.add(keycode);

        if (downKeys.size >= 2) {
            return multipleKeysDown(keycode);
        }

        return false;
    }

    public boolean multipleKeysDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        downKeys.remove(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        downKeys.add(button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        downKeys.remove(button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // Zoom out
        if (amount == 1) {
            cameraSystem.camera.zoom += ZOOM_SPEED;
            cameraSystem.camera.zoom = MathUtils.clamp(cameraSystem.camera.zoom, MIN_ZOOM, MAX_ZOOM);
        }

        // Zoom in
        if (amount == -1) {
            cameraSystem.camera.zoom -= ZOOM_SPEED;
            cameraSystem.camera.zoom = MathUtils.clamp(cameraSystem.camera.zoom, MIN_ZOOM, MAX_ZOOM);
        }

        return true;
    }
}
