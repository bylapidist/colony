package net.lapidist.colony.core.systems.logic;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.components.PlayerComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import java.util.Optional;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends EntityProcessingSystem implements InputProcessor {

    private final static float MOVEMENT_SPEED = 10f;
    private final static float MIN_ZOOM = 1f;
    private final static float MAX_ZOOM = 2f;
    private final static float ZOOM_SPEED = 0.06f;

    private IntSet downKeys = new IntSet(20);
    private Entity entity;
    private CameraSystem cameraSystem;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerComponent.class));

        Colony.getInputMultiplexer().addProcessor(this);
    }

    private void processInput() {
        Vector2 oldPosition = new Vector2(
                E(entity).getSpriteComponent().getSprite().getX(),
                E(entity).getSpriteComponent().getSprite().getY()
        );

        if (singleKeyDown(Input.Keys.W)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x, oldPosition.y - MOVEMENT_SPEED);
        }

        if (singleKeyDown(Input.Keys.A)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - MOVEMENT_SPEED, oldPosition.y);
        }

        if (singleKeyDown(Input.Keys.S)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x, oldPosition.y + MOVEMENT_SPEED);
        }

        if (singleKeyDown(Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + MOVEMENT_SPEED, oldPosition.y);
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + MOVEMENT_SPEED, oldPosition.y - MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - MOVEMENT_SPEED, oldPosition.y - MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + MOVEMENT_SPEED, oldPosition.y + MOVEMENT_SPEED);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - MOVEMENT_SPEED, oldPosition.y + MOVEMENT_SPEED);
        }
    }

    private boolean singleKeyDown(int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    private boolean twoKeysDown(int keycode1, int keycode2) {
        return downKeys.contains(keycode1) && downKeys.contains(keycode2) && downKeys.size == 2;
    }

    private void updatePlayerTile() {
        Vector2 position = new Vector2(
                E(entity).getSpriteComponent().getSprite().getX(),
                E(entity).getSpriteComponent().getSprite().getY()
        );

//        Optional<ITile> playerTile = E().grid
    }

    @Override
    protected void process(Entity e) {
        this.entity = e;

        processInput();
        updatePlayerTile();
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
