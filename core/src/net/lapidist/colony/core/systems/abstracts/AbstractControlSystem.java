package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.core.views.ViewController;

public abstract class AbstractControlSystem extends IteratingSystem implements InputProcessor {

    protected final static float MIN_ZOOM = 1f;
    protected final static float MAX_ZOOM = 2f;
    protected final static float ZOOM_SPEED = 0.06f;
    protected final static InputMultiplexer inputMultiplexer = new InputMultiplexer();
    public final static ViewController viewController = new ViewController();

    private IntSet downKeys = new IntSet(20);

    public AbstractControlSystem(Aspect.Builder aspect) {
        super(aspect);
    }

    protected boolean singleKeyDown(int keycode) {
        return downKeys.contains(keycode) && downKeys.size == 1;
    }

    protected boolean twoKeysDown(int keycode1, int keycode2) {
        return downKeys.contains(keycode1) && downKeys.contains(keycode2) && downKeys.size == 2;
    }

    protected boolean multipleKeysDown(int lastKeycode) {
        return false;
    }

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void process(int entityId) {
    }

    @Override
    public boolean keyDown(int keycode) {
        downKeys.add(keycode);

        if (downKeys.size >= 2) {
            return multipleKeysDown(keycode);
        }

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
