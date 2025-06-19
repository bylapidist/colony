package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.entities.PlayerFactory;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.CelestialSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.SeasonCycleSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.network.ChunkRequestQueueSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.components.state.map.PlayerPosition;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.settings.KeyBindings;
import net.mostlyoriginal.api.event.common.EventSystem;

/** Builder for the update-only logic world. */
public final class LogicWorldBuilder {

    private LogicWorldBuilder() {
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final KeyBindings keyBindings
    ) {
        return baseBuilder(client, keyBindings, new ResourceData());
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final KeyBindings keyBindings,
            final ResourceData playerResources
    ) {
        MapState state = MapState.builder()
                .playerResources(playerResources)
                .build();
        return createBuilder(client, keyBindings, null, state, null);
    }

    public static WorldConfigurationBuilder builder(
            final MapStateProvider provider,
            final GameClient client,
            final KeyBindings keyBindings,
            final java.util.function.Consumer<Float> callback
    ) {
        return createBuilder(client, keyBindings, provider, new MapState(), callback);
    }

    public static WorldConfigurationBuilder builder(
            final MapState state,
            final GameClient client,
            final KeyBindings keyBindings,
            final java.util.function.Consumer<Float> callback
    ) {
        return createBuilder(
                client,
                keyBindings,
                new ProvidedMapStateProvider(state),
                state,
                callback
        );
    }

    private static WorldConfigurationBuilder createBuilder(
            final GameClient client,
            final KeyBindings keyBindings,
            final MapStateProvider provider,
            final MapState state,
            final java.util.function.Consumer<Float> callback
    ) {
        MutableEnvironmentState environment = new MutableEnvironmentState(state.environment());
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        new CameraInputSystem(client, keyBindings),
                        new SelectionSystem(client, keyBindings),
                        new BuildPlacementSystem(client, keyBindings),
                        new PlayerMovementSystem(client, keyBindings),
                        new TileUpdateSystem(client),
                        new BuildingUpdateSystem(client),
                        new ResourceUpdateSystem(client),
                        new ChunkLoadSystem(client),
                        new ChunkRequestQueueSystem(client),
                        new CelestialSystem(client, environment),
                        new SeasonCycleSystem(environment)
                );
        if (provider != null) {
            builder.with(new net.lapidist.colony.client.systems.MapInitSystem(provider, true, callback));
        }
        return builder;
    }

    public static World build(
            final WorldConfigurationBuilder builder,
            final GameClient client,
            final ResourceData playerResources,
            final PlayerPosition playerPos
    ) {
        builder.with(new PlayerCameraSystem());
        World world = new World(builder.build());
        PlayerFactory.create(world, client, playerResources, playerPos);
        Events.init(world.getSystem(EventSystem.class));
        return world;
    }
}
