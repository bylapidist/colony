package net.lapidist.colony.core.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.core.ComponentMappers;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.components.SpriteComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.events.EventType;
import net.lapidist.colony.core.systems.MapRenderingSystem;

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
            downKeys.add(Input.Buttons.LEFT);

            for (Entity entity : renderingSystem.getRenderQueue()) {
                TileComponent tileC = ComponentMappers.tiles.get(entity);
                SpriteComponent spriteC = ComponentMappers.sprites.get(entity);

                if (spriteC.sprite.getBoundingRectangle().contains(Camera.worldCoords(screenX, screenY))) {
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
        if (downKeys.contains(Input.Buttons.LEFT)) {
            downKeys.remove(Input.Buttons.LEFT);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (Entity entity : renderingSystem.getRenderQueue()) {
            TileComponent tileC = ComponentMappers.tiles.get(entity);
            Ray ray = Core.camera.getPickRay(screenX, screenY);

            Vector3 center = new Vector3(
                    tileC.tile.getBoundingBox().x,
                    tileC.tile.getBoundingBox().y,
                    0
            );

            if (Intersector.intersectRaySphere(ray, center, Constants.PPM / 1.4f, null)) {
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
