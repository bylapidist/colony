package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.InputAdapter;

/**
 * Processes scroll wheel input events.
 */
public final class ScrollInputProcessor extends InputAdapter {

    private final GestureInputHandler gestureHandler;

    public ScrollInputProcessor(final GestureInputHandler gestureHandlerToSet) {
        this.gestureHandler = gestureHandlerToSet;
    }

    @Override
    public boolean scrolled(final float amountX, final float amountY) {
        return gestureHandler.scrolled(amountX, amountY);
    }
}
