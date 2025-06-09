package net.lapidist.colony.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.components.GameConstants;

/**
 * Utility methods for converting between tile, world and screen coordinates.
 */
public final class CameraUtils {

    private CameraUtils() {
    }

    public static Vector2 tileCoordsToWorldCoords(final int x, final int y) {
        return new Vector2(x * GameConstants.TILE_SIZE, y * GameConstants.TILE_SIZE);
    }

    public static Vector2 tileCoordsToWorldCoords(final Vector2 coords) {
        return tileCoordsToWorldCoords((int) coords.x, (int) coords.y);
    }

    public static Vector2 worldCoordsToTileCoords(final int x, final int y) {
        return new Vector2(
                Math.floorDiv(x, GameConstants.TILE_SIZE),
                Math.floorDiv(y, GameConstants.TILE_SIZE)
        );
    }

    public static Vector2 worldCoordsToTileCoords(final Vector2 coords) {
        return worldCoordsToTileCoords((int) coords.x, (int) coords.y);
    }

    public static Vector2 screenToWorldCoords(
            final ExtendViewport viewport,
            final float screenX,
            final float screenY
    ) {
        return viewport.unproject(new Vector2(screenX, screenY));
    }

    public static Vector2 worldToScreenCoords(
            final ExtendViewport viewport,
            final float worldX,
            final float worldY
    ) {
        Vector3 tmp = viewport.project(new Vector3(worldX, worldY, 0));
        return new Vector2(tmp.x, tmp.y);
    }

    public static boolean withinCameraView(
            final ExtendViewport viewport,
            final Vector2 worldCoords
    ) {
        Vector3 tmp = viewport.project(new Vector3(worldCoords.x, worldCoords.y, 0));
        return !(tmp.x > Gdx.graphics.getWidth() || tmp.y > Gdx.graphics.getHeight());
    }

    public static Vector2 getWorldCenter() {
        return new Vector2(
                (GameConstants.TILE_SIZE * GameConstants.MAP_WIDTH + GameConstants.TILE_SIZE) / 2f,
                (GameConstants.TILE_SIZE * GameConstants.MAP_HEIGHT + GameConstants.TILE_SIZE) / 2f
        );
    }

    public static Rectangle getViewBounds(
            final OrthographicCamera camera,
            final ExtendViewport viewport,
            final Rectangle out
    ) {
        float halfWidth = viewport.getWorldWidth() * camera.zoom / 2f;
        float halfHeight = viewport.getWorldHeight() * camera.zoom / 2f;
        out.set(
                camera.position.x - halfWidth,
                camera.position.y - halfHeight,
                halfWidth * 2f,
                halfHeight * 2f
        );
        return out;
    }
}
