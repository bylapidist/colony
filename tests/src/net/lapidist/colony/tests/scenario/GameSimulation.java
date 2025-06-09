package net.lapidist.colony.tests.scenario;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.components.state.MapState;

import static org.mockito.Mockito.mock;

/**
 * Utility for running a small game simulation with a real {@link World}.
 * Systems can be stepped and inputs triggered to verify game logic.
 */
public final class GameSimulation {
    private final World world;
    private final InputSystem inputSystem;
    private final PlayerCameraSystem cameraSystem;
    private final GameClient client;

    private static final float STEP_TIME = 1f / 60f;
    /**
     * Create a new simulation world with a mock {@link GameClient}.
     *
     * @param state initial map state to load
     */
    public GameSimulation(final MapState state) {
        this(state, mock(GameClient.class));
    }

    /**
     * Create a new simulation world using the provided {@link GameClient}.
     */
    public GameSimulation(final MapState state, final GameClient clientToUse) {
        client = clientToUse;
        world = new World(new WorldConfigurationBuilder()
                .with(
                        new MapLoadSystem(state),
                        new PlayerCameraSystem(),
                        new InputSystem(client, new net.lapidist.colony.settings.Settings()),
                        new TileUpdateSystem(client),
                        new BuildingUpdateSystem(client),
                        new ResourceUpdateSystem(client)
                )
                .build());
        // run once so systems initialise
        world.process();
        inputSystem = world.getSystem(InputSystem.class);
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
    }

    /** Process one frame with a fixed delta time. */
    public void step() {
        world.setDelta(STEP_TIME);
        world.process();
    }

    public World getWorld() {
        return world;
    }

    public InputSystem getInput() {
        return inputSystem;
    }

    public PlayerCameraSystem getCamera() {
        return cameraSystem;
    }

    public GameClient getClient() {
        return client;
    }
}
