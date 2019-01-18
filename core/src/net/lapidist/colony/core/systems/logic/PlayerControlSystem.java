package net.lapidist.colony.core.systems.logic;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.components.PlayerComponent;
import net.lapidist.colony.components.VelocityComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.HoverTileOutsideReachEvent;
import net.lapidist.colony.core.events.HoverTileWithinReachEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends EntityProcessingSystem implements InputProcessor {

    private final static float BASE_ACCELERATION = 350f;
    private final static float BASE_MAX_VELOCITY = 100f;
    private final static float BASE_FRICTION = 150f;
    private final static float MIN_VELOCITY = 0.5f;
    private final static float MIN_ZOOM = 1f;
    private final static float MAX_ZOOM = 2f;
    private final static float ZOOM_SPEED = 0.06f;
    private final static float REACH = (5f * Constants.PPM) / 2f;

    private IntSet downKeys = new IntSet(20);
    private Entity e;
    private Vector2 position;
    private Vector2 origin;
    private Vector2 tmpPosition;
    private Vector2 tmpVelocity;
    private Rectangle mapBounds;
    private Circle reachBounds;
    private CameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerComponent.class, VelocityComponent.class));

        tmpPosition = new Vector2();
        tmpVelocity = new Vector2();
        mapBounds = new Rectangle();
        reachBounds = new Circle();

        Colony.getInputMultiplexer().addProcessor(this);
    }

    private void processInput() {
        tmpPosition = tmpPosition.set(position.x, position.y);
        tmpVelocity = E(e).getVelocityComponent().getVelocity();

        if (singleKeyDown(Input.Keys.W)) {
            tmpVelocity.y += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (singleKeyDown(Input.Keys.A)) {
            tmpVelocity.x += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (singleKeyDown(Input.Keys.S)) {
            tmpVelocity.y += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (singleKeyDown(Input.Keys.D)) {
            tmpVelocity.x += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            tmpVelocity.y += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
            tmpVelocity.x += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            tmpVelocity.x += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
            tmpVelocity.y += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            tmpVelocity.y += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
            tmpVelocity.x += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            tmpVelocity.x += BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
            tmpVelocity.y += -BASE_ACCELERATION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (tmpVelocity.x > 0) {
            tmpVelocity.x -= BASE_FRICTION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (tmpVelocity.x < 0) {
            tmpVelocity.x -= -BASE_FRICTION * Gdx.graphics.getDeltaTime();
            tmpPosition.x -= tmpVelocity.x * Gdx.graphics.getDeltaTime();
        }

        if (tmpVelocity.y > 0) {
            tmpVelocity.y -= BASE_FRICTION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (tmpVelocity.y < 0) {
            tmpVelocity.y -= -BASE_FRICTION * Gdx.graphics.getDeltaTime();
            tmpPosition.y -= tmpVelocity.y * Gdx.graphics.getDeltaTime();
        }

        if (tmpVelocity.x < -BASE_MAX_VELOCITY) {
            tmpVelocity.x = -BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.y < -BASE_MAX_VELOCITY) {
            tmpVelocity.y = -BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.x > BASE_MAX_VELOCITY) {
            tmpVelocity.x = BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.y > BASE_MAX_VELOCITY) {
            tmpVelocity.y = BASE_MAX_VELOCITY;
        }

        if (tmpVelocity.x < MIN_VELOCITY && tmpVelocity.x > -MIN_VELOCITY) {
            tmpVelocity.x = 0;
        }

        if (tmpVelocity.y < MIN_VELOCITY && tmpVelocity.y > -MIN_VELOCITY) {
            tmpVelocity.y = 0;
        }

        if (!isWithinGrid(tmpPosition)) {
            E(e).getVelocityComponent().setVelocity(tmpVelocity.set(0, 0));
            return;
        }

        System.out.println(tmpVelocity);

        E(e).getVelocityComponent().setVelocity(tmpVelocity);
        E(e).spriteComponentSprite().setPosition(tmpPosition.x, tmpPosition.y);
    }

    private boolean singleKeyDown(int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    private boolean twoKeysDown(int keycode1, int keycode2) {
        return downKeys.contains(keycode1) && downKeys.contains(keycode2) && downKeys.size == 2;
    }

    private Vector2 getPlayerPosition() {
        Sprite sprite = E(e).getSpriteComponent().getSprite();

        return new Vector2(
                sprite.getX(),
                sprite.getY()
        );
    }

    private Vector2 getPlayerOrigin() {
        Sprite sprite = E(e).getSpriteComponent().getSprite();

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

        E(e).cellComponentCell(cell);
    }

    private boolean isWithinGrid(Vector2 position) {
        return mapBounds.contains(position);
    }

    private boolean isWithinReach(Vector2 position) {
        return reachBounds.contains(position);
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
        this.e = e;
        position = getPlayerPosition();
        origin = getPlayerOrigin();
        reachBounds.set(origin, REACH);

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

    public boolean multipleKeysDown(int lastKeycode) {
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
        tmpPosition.set(cameraSystem.worldCoords(screenX, screenY));

        int estimatedGridX = (int) tmpPosition.x / mapGenerationSystem.getTileWidth();
        int estimatedGridY = (int) tmpPosition.y / mapGenerationSystem.getTileHeight();

        if (isWithinReach(tmpPosition.set(tmpPosition.x, tmpPosition.y))) {
            Events.fire(new HoverTileWithinReachEvent(estimatedGridX, estimatedGridY));
            return true;
        }

        Events.fire(new HoverTileOutsideReachEvent(estimatedGridX, estimatedGridY));
        return true;
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
