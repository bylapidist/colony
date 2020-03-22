package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntSet;

public abstract class AbstractControlSystem
        extends IteratingSystem implements InputProcessor {

    protected static final float MIN_ZOOM = 1f;
    protected static final float MAX_ZOOM = 2f;
    protected static final float ZOOM_SPEED = 0.06f;

    private static final InputMultiplexer INPUT_MULTIPLEXER =
            new InputMultiplexer();
    private static final IntSet DOWN_KEYS =
            new IntSet(20);

    public AbstractControlSystem(final Aspect.Builder aspect) {
        super(aspect);
    }

    public static InputMultiplexer getInputMultiplexer() {
        return INPUT_MULTIPLEXER;
    }

    protected final boolean singleKeyDown(final int keycode) {
        return DOWN_KEYS.contains(keycode) && DOWN_KEYS.size == 1;
    }

    protected final boolean twoKeysDown(
            final int keycode1,
            final int keycode2
    ) {
        return DOWN_KEYS.contains(keycode1)
                && DOWN_KEYS.contains(keycode2)
                && DOWN_KEYS.size == 2;
    }

    protected final boolean multipleKeysDown(final int lastKeycode) {
        return false;
    }

    @Override
    protected final void initialize() {
        Gdx.input.setInputProcessor(INPUT_MULTIPLEXER);
        initializePlayer();
    }

    protected abstract void initializePlayer();

    @Override
    protected void process(final int entityId) {
    }

    @Override
    public final boolean keyDown(final int keycode) {
        DOWN_KEYS.add(keycode);

        if (DOWN_KEYS.size >= 2) {
            return multipleKeysDown(keycode);
        }

        return false;
    }

    @Override
    public final boolean keyUp(final int keycode) {
        DOWN_KEYS.remove(keycode);
        return false;
    }

    @Override
    public final boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public final boolean touchDown(
            final int screenX,
            final int screenY,
            final int pointer,
            final int button
    ) {
        DOWN_KEYS.add(button);

        touchDownPlayer(
                screenX,
                screenY,
                pointer,
                button
        );

        return false;
    }

    protected abstract boolean touchDownPlayer(
            int screenX,
            int screenY,
            int pointer,
            int button
    );

    @Override
    public final boolean touchUp(
            final int screenX,
            final int screenY,
            final int pointer,
            final int button
    ) {
        DOWN_KEYS.remove(button);

        return false;
    }

    @Override
    public final boolean touchDragged(
            final int screenX,
            final int screenY,
            final int pointer
    ) {
        return false;
    }

    @Override
    public final boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public final boolean scrolled(final int amount) {
        scrolledPlayer(amount);
        return false;
    }

    protected abstract boolean scrolledPlayer(int amount);
}
