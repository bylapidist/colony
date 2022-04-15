package net.lapidist.colony.client.core.utils;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;

public final class World {

    private World() {
    }

    public static Vector2 tileCoordsToWorldCoords(final int x, final int y) {
        return new Vector2(
               x * Constants.TILE_SIZE,
               y * Constants.TILE_SIZE
        );
    }

    public static Vector2 tileCoordsToWorldCoords(final Vector2 coords) {
        return tileCoordsToWorldCoords((int) coords.x, (int) coords.y);
    }

    public static Vector2 worldCoordsToTileCoords(final int x, final int y) {
        return new Vector2(
                Math.floorDiv(x, Constants.TILE_SIZE),
                Math.floorDiv(y, Constants.TILE_SIZE)
        );
    }

    public static Vector2 worldCoordsToTileCoords(final Vector2 coords) {
        return worldCoordsToTileCoords((int) coords.x, (int) coords.y);
    }
}
