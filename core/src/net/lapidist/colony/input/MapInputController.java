package net.lapidist.colony.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.component.DecalComponent;
import net.lapidist.colony.component.TileComponent;
import net.lapidist.colony.core.Camera;
import net.lapidist.colony.core.Core;
import net.lapidist.colony.event.EventType;
import net.lapidist.colony.event.Events;
import net.lapidist.colony.system.MapRenderingSystem;

import static net.lapidist.colony.ComponentMappers.*;
import static net.lapidist.colony.Constants.PPM;

public class MapInputController implements InputProcessor {

    private final IntSet downKeys = new IntSet(20);
    private final MapRenderingSystem renderingSystem;

    private TileComponent selectedTile;
    private TileComponent hoveredTile;

    public MapInputController(MapRenderingSystem renderingSystem) {
        this.renderingSystem = renderingSystem;
    }

    public TileComponent getSelectedTile() {
        return selectedTile;
    }

    public TileComponent getHoveredTile() {
        return hoveredTile;
    }

    @Override
    public boolean keyDown(int keycode) {
        downKeys.add(keycode);

        if (downKeys.size >= 2) {
            multipleKeysDown(keycode);
        } else {
            if (keycode == Input.Keys.W) {
                Core.camera.setState(Camera.CameraState.PANNING_N);
            }

            if (keycode == Input.Keys.A) {
                Core.camera.setState(Camera.CameraState.PANNING_W);
            }

            if (keycode == Input.Keys.S) {
                Core.camera.setState(Camera.CameraState.PANNING_S);
            }

            if (keycode == Input.Keys.D) {
                Core.camera.setState(Camera.CameraState.PANNING_E);
            }
        }

        return false;
    }

    private void multipleKeysDown(int mostRecentKeycode) {
        if (downKeys.contains(Input.Keys.W) && downKeys.contains(Input.Keys.D)) {
            Core.camera.setState(Camera.CameraState.PANNING_NE);
        }

        if (downKeys.contains(Input.Keys.A) && downKeys.contains(Input.Keys.W)) {
            Core.camera.setState(Camera.CameraState.PANNING_NW);
        }

        if (downKeys.contains(Input.Keys.S) && downKeys.contains(Input.Keys.D)) {
            Core.camera.setState(Camera.CameraState.PANNING_SE);
        }

        if (downKeys.contains(Input.Keys.A) && downKeys.contains(Input.Keys.S)) {
            Core.camera.setState(Camera.CameraState.PANNING_SW);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W || keycode == Input.Keys.A || keycode == Input.Keys.S || keycode == Input.Keys.D) {
            Core.camera.setState(Camera.CameraState.STATIC);
        }

        downKeys.remove(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            for (Entity entity : renderingSystem.getRenderQueue()) {
                TileComponent tileC = tiles.get(entity);
                Ray ray = Core.camera.getPickRay(screenX, screenY);

                Vector3 center = new Vector3(
                    tileC.bounds.getBoundingRectangle().x,
                    tileC.bounds.getBoundingRectangle().y,
                    0
                );

                if (Intersector.intersectRaySphere(ray, center, PPM / 1.4f, null)) {
                    Events.fire(new EventType.TileClickEvent(tileC));

                    if (!tileC.active) {
                        tileC.active = true;
                        selectedTile = tileC;
                    }
                }
            }
        }

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
        for (Entity entity : renderingSystem.getRenderQueue()) {
            TileComponent tileC = tiles.get(entity);
            Ray ray = Core.camera.getPickRay(screenX, screenY);

            Vector3 center = new Vector3(
                tileC.bounds.getBoundingRectangle().x,
                tileC.bounds.getBoundingRectangle().y,
                0
            );

            if (Intersector.intersectRaySphere(ray, center, PPM / 1.4f, null)) {
                if (!tileC.hovered) {
                    tileC.hovered = true;
                    hoveredTile = tileC;
                }
            }
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 1) {
            Core.camera.setState(Camera.CameraState.ZOOMING_OUT);
        }

        if (amount == -1) {
            Core.camera.setState(Camera.CameraState.ZOOMING_IN);
        }

        return true;
    }
}
