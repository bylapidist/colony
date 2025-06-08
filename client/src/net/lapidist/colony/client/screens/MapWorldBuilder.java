package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.InputSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.UISystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.core.events.Events;
import net.mostlyoriginal.api.event.common.EventSystem;

/**
 * Builds the {@link World} used by {@link MapScreen}.
 */
public final class MapWorldBuilder {

    private MapWorldBuilder() {
    }

    /**
     * Create a base {@link WorldConfigurationBuilder} containing the default
     * systems for the map screen except the map initialization system. Tests can
     * customise the returned builder before calling {@link #build(WorldConfigurationBuilder)}.
     *
     * @param client game client for network updates
     * @param stage  stage used by the UI system
     * @return configured builder instance
     */
    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final Stage stage
    ) {
        InputSystem inputSystem = new InputSystem(client);
        inputSystem.addProcessor(stage);

        return new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        new ClearScreenSystem(Color.BLACK),
                        inputSystem,
                        new TileUpdateSystem(client),
                        new MapRenderSystem(new MapRendererFactory()),
                        new UISystem(stage)
                );
    }

    /**
     * Create a {@link WorldConfigurationBuilder} with the default systems and a
     * map state provider.
     *
     * @param provider map state provider to load
     * @param client   game client for network updates
     * @param stage    stage used by the UI system
     * @return configured builder instance
     */
    public static WorldConfigurationBuilder builder(
            final MapStateProvider provider,
            final GameClient client,
            final Stage stage
    ) {
        return baseBuilder(client, stage)
                .with(
                        new MapInitSystem(provider),
                        new PlayerCameraSystem()
                );
    }

    /**
     * Convenience overload using a pre-built {@link MapState}.
     */
    public static WorldConfigurationBuilder builder(
            final MapState state,
            final GameClient client,
            final Stage stage
    ) {
        return builder(new ProvidedMapStateProvider(state), client, stage);
    }

    /**
     * Build a world using the supplied configuration.
     *
     * @param builder preconfigured world builder with all desired systems
     * @return configured world instance
     */
    public static World build(final WorldConfigurationBuilder builder) {
        World world = new World(builder.build());
        Events.init(world.getSystem(EventSystem.class));
        return world;
    }
}
