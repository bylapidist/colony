package net.lapidist.colony.tests.core.utils;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.utils.World;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorldTest {

    private final int testX = 100;

    private final int testY = 200;

    private final Vector2 testCoords = new Vector2(testX, testY);

    private final Vector2 testWorldCoords = new Vector2(3200, 6400);

    private final Vector2 testTileCoords = new Vector2(3, 6);

    @Test
    public final void testTileCoordsToWorldCoords() {
        assertEquals(
                World.tileCoordsToWorldCoords(testX, testY),
                testWorldCoords
        );
        assertEquals(
                World.tileCoordsToWorldCoords(testCoords),
                testWorldCoords
        );
    }

    @Test
    public final void testWorldCoordsToTileCoords() {
        assertEquals(
                World.worldCoordsToTileCoords(testX, testY),
                testTileCoords
        );
        assertEquals(
                World.worldCoordsToTileCoords(testCoords),
                testTileCoords
        );
    }
}
