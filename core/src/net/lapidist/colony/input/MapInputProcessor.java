package net.lapidist.colony.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.Colony;
import net.lapidist.colony.screen.GameScreen;

public class MapInputProcessor implements InputProcessor {

    private OrthographicCamera camera;
    private GameScreen screen;
    private float initialScale;

    /**
     * Constructor for the MapInputProcessor.
     *
     * @param screen Screen.
     */
    public MapInputProcessor(GameScreen screen) {
        this.screen = screen;
        this.camera = screen.getCamera();

        initialScale = camera.zoom;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            Colony.game.entityManager.dispose();

//            new FlatMap(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, Constants.MAP_DEPTH);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
        // Zoom in
        if (amount == 1) {
            if (camera.zoom < 0.5f) {
                camera.zoom = 0.5f;
                return true;
            }
            if (camera.zoom < 3.5f) {
                camera.zoom += GameScreen.ZOOM_SPEED * 4;
                return true;
            }
        }

        // Zoom out
        if (amount == -1) {
            if (camera.zoom > 3.5f) {
                camera.zoom = 3.5f;
                return true;
            }
            if (camera.zoom > 0.5f) {
                camera.zoom -= GameScreen.ZOOM_SPEED * 4;
                return true;
            }
        }
        return false;
    }
}