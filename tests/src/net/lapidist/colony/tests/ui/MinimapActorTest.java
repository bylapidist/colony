package net.lapidist.colony.tests.ui;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Basic tests for {@link MinimapActor}.
 */
@RunWith(GdxTestRunner.class)
public class MinimapActorTest {

    @Test
    public void drawsWithoutErrors() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem())
                .build());
        world.process();

        MinimapActor minimap = new MinimapActor(world);
        minimap.dispose();
    }
}
