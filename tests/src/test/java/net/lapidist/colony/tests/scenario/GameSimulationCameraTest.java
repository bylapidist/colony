package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;

/**
 * Example scenario test verifying camera movement when panning.
 */
@RunWith(GdxTestRunner.class)
public class GameSimulationCameraTest {

    @Test
    public void panMovesCamera() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.putTile(tile);

        GameSimulation sim = new GameSimulation(state);
        float startX = sim.getCamera().getCamera().position.x;
        float startY = sim.getCamera().getCamera().position.y;

        final float deltaX = -20f;
        final float deltaY = 15f;
        sim.getCameraInput().pan(0, 0, deltaX, deltaY);
        sim.step();

        final float epsilon = 0.01f;
        float expectedX = max(0, min(MapState.DEFAULT_WIDTH * GameConstants.TILE_SIZE, startX - deltaX));
        float expectedY = max(0, min(MapState.DEFAULT_HEIGHT * GameConstants.TILE_SIZE, startY + deltaY));

        assertEquals(expectedX, sim.getCamera().getCamera().position.x, epsilon);
        assertEquals(expectedY, sim.getCamera().getCamera().position.y, epsilon);
    }
}
