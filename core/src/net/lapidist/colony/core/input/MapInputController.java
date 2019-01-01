package net.lapidist.colony.core.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntSet;
import net.lapidist.colony.core.ComponentMappers;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.events.EventType;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.core.systems.MapRenderingSystem;

public class MapInputController implements InputProcessor {

    private final IntSet downKeys = new IntSet(20);
    private final MapRenderingSystem renderingSystem;

    private TileComponent selectedTile;
    private TileComponent hoveredTile;

    private int lastX = 0;
    private int lastY = 0;
    private Matrix4 tempMat = new Matrix4();
    private Vector3 screenAOR = new Vector3();
    private Vector3 worldAOR = new Vector3();

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
                Ray ray = Core.camera.getPickRay(screenX, screenY);

                Vector3 center = new Vector3(
                    tileC.tile.getBoundingBox().x,
                    tileC.tile.getBoundingBox().y,
                    0
                );

                if (Intersector.intersectRaySphere(ray, center, Constants.PPM / 1.4f, null)) {
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
        lastX -= screenX;
        lastY -= screenY;

        float screenAng = Vector3.len(lastX, lastY, 0f);

        screenAOR.set(lastY / screenAng, lastX / screenAng, 0f); // direction vector of the AOR

        if (downKeys.contains(Input.Buttons.LEFT)) {
            Core.camera.setState(Camera.CameraState.ROTATING_CLOCKWISE);
            // rotate the camera
            // transform the angle of rotation from screen CS to camera CS
            // get the camera transformation matrix
            tempMat.set(Core.camera.view);
            tempMat.translate(Core.camera.position);
            tempMat.inv();

            // transform the screen AOR to a world AOR
            worldAOR = transform(tempMat, screenAOR, worldAOR).nor();
            worldAOR = worldAOR.scl(0.5f);

            // apply the rotation of the angle about the world AOR to the camera
            Core.camera.rotateAround(Vector3.Zero, worldAOR, screenAng / 2.5f);
            Core.camera.update();
        }

        return false;
    }

    private Vector3 transform(Matrix4 mat, Vector3 from, Vector3 to) {
        // transform a vector according to a transformation matrix
        // https://stackoverflow.com/questions/53788293/how-do-i-rotate-objects-using-mouse-input-in-libgdx

        to.x = from.dot(mat.val[ Matrix4.M00], mat.val[Matrix4.M01],
            mat.val[Matrix4.M02]) + mat.val[Matrix4.M03];
        to.y = from.dot(mat.val[Matrix4.M10], mat.val[ Matrix4.M11],
            mat.val[Matrix4.M12]) + mat.val[ Matrix4.M13];
        to.z = from.dot(mat.val[Matrix4.M20], mat.val[Matrix4.M21],
            mat.val[Matrix4.M22]) + mat.val[Matrix4.M23];

        return to;
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
