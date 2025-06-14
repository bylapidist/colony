package net.lapidist.colony.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lapidist.colony.components.GameConstants;

/**
 * Utility methods for converting between tile, world and screen coordinates.
 */
public final class CameraUtils {

    private CameraUtils() {
    }

    public static Vector2 tileCoordsToWorldCoords(final int x, final int y) {
        return tileCoordsToWorldCoords(x, y, new Vector2());
    }

    public static Vector2 tileCoordsToWorldCoords(final Vector2 coords) {
        return tileCoordsToWorldCoords((int) coords.x, (int) coords.y, new Vector2());
    }

    public static Vector2 tileCoordsToWorldCoords(final int x, final int y, final Vector2 out) {
        out.set(x * GameConstants.TILE_SIZE, y * GameConstants.TILE_SIZE);
        return out;
    }

    public static Vector2 tileCoordsToWorldCoords(final Vector2 coords, final Vector2 out) {
        return tileCoordsToWorldCoords((int) coords.x, (int) coords.y, out);
    }

    public static Vector2 worldCoordsToTileCoords(final int x, final int y) {
        return worldCoordsToTileCoords(x, y, new Vector2());
    }

    public static Vector2 worldCoordsToTileCoords(final Vector2 coords) {
        return worldCoordsToTileCoords((int) coords.x, (int) coords.y, new Vector2());
    }

    public static Vector2 worldCoordsToTileCoords(final int x, final int y, final Vector2 out) {
        out.set(
                Math.floorDiv(x, GameConstants.TILE_SIZE),
                Math.floorDiv(y, GameConstants.TILE_SIZE)
        );
        return out;
    }

    public static Vector2 worldCoordsToTileCoords(final Vector2 coords, final Vector2 out) {
        return worldCoordsToTileCoords((int) coords.x, (int) coords.y, out);
    }

    public static Vector2 screenToWorldCoords(
            final Viewport viewport,
            final float screenX,
            final float screenY
    ) {
        return screenToWorldCoords(viewport, screenX, screenY, new Vector2());
    }

    public static Vector2 screenToWorldCoords(
            final Viewport viewport,
            final float screenX,
            final float screenY,
            final Vector2 out
    ) {
        out.set(screenX, screenY);
        viewport.unproject(out);
        return out;
    }

    public static Vector2 worldToScreenCoords(
            final Viewport viewport,
            final float worldX,
            final float worldY
    ) {
        return worldToScreenCoords(viewport, worldX, worldY, new Vector2());
    }

    public static Vector2 worldToScreenCoords(
            final Viewport viewport,
            final float worldX,
            final float worldY,
            final Vector2 out
    ) {
        return worldToScreenCoords(viewport, worldX, worldY, out, new Vector3());
    }

    public static Vector2 worldToScreenCoords(
            final Viewport viewport,
            final float worldX,
            final float worldY,
            final Vector2 out,
            final Vector3 tmp
    ) {
        tmp.set(worldX, worldY, 0);
        viewport.project(tmp);
        out.set(tmp.x, tmp.y);
        return out;
    }

    public static boolean withinCameraView(
            final Viewport viewport,
            final Vector2 worldCoords
    ) {
        return withinCameraView(viewport, worldCoords, new Vector3());
    }

    public static boolean withinCameraView(
            final Viewport viewport,
            final Vector2 worldCoords,
            final Vector3 tmp
    ) {
        tmp.set(worldCoords.x, worldCoords.y, 0);
        viewport.project(tmp);
        return !(tmp.x > Gdx.graphics.getWidth() || tmp.y > Gdx.graphics.getHeight());
    }

    public static Vector2 getWorldCenter(final int mapWidth, final int mapHeight) {
        return new Vector2(
                (GameConstants.TILE_SIZE * mapWidth + GameConstants.TILE_SIZE) / 2f,
                (GameConstants.TILE_SIZE * mapHeight + GameConstants.TILE_SIZE) / 2f
        );
    }

    public static Vector2 getWorldCenter() {
        return getWorldCenter(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT);
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

    /**
     * Returns {@code true} if the provided world coordinate lies within the
     * specified camera view bounds.
     *
     * @param view   world-space camera view rectangle
     * @param worldX world x coordinate
     * @param worldY world y coordinate
     * @return {@code true} when the coordinate is visible
     */
    public static boolean isVisible(final Rectangle view, final float worldX, final float worldY) {
        return view.contains(worldX, worldY);
    }
}
