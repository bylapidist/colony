package net.lapidist.colony.core.systems.logic;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.components.PlayerComponent;
import net.lapidist.colony.core.Colony;

import static com.artemis.E.E;

@Wire
public class PlayerControlSystem extends EntityProcessingSystem implements InputProcessor {

    private IntSet downKeys = new IntSet(20);
    private enum Direction {
        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST
    }
    private Entity entity;
    private float movementSpeed = 10f;
    private Direction currentDirection;

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
                    .setPosition(oldPosition.x, oldPosition.y - movementSpeed);
        }

        if (singleKeyDown(Input.Keys.A)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - movementSpeed, oldPosition.y);
        }

        if (singleKeyDown(Input.Keys.S)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x, oldPosition.y + movementSpeed);
        }

        if (singleKeyDown(Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + movementSpeed, oldPosition.y);
        }

        if (twoKeysDown(Input.Keys.W, Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + movementSpeed, oldPosition.y - movementSpeed);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.W)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - movementSpeed, oldPosition.y - movementSpeed);
        }

        if (twoKeysDown(Input.Keys.S, Input.Keys.D)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x + movementSpeed, oldPosition.y + movementSpeed);
        }

        if (twoKeysDown(Input.Keys.A, Input.Keys.S)) {
            E(entity)
                    .getSpriteComponent().getSprite()
                    .setPosition(oldPosition.x - movementSpeed, oldPosition.y + movementSpeed);
        }
    }

    private boolean singleKeyDown(int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    private boolean twoKeysDown(int keycode1, int keycode2) {
        return downKeys.contains(keycode1) && downKeys.contains(keycode2) && downKeys.size == 2;
    }

    @Override
    protected void process(Entity e) {
        this.entity = e;

        processInput();
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
        return false;
    }
}
