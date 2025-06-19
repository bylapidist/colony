package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.entities.PlayerFactory;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.network.ChunkRequestQueueSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.PlayerPosition;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.KeyBindings;
import net.mostlyoriginal.api.event.common.EventSystem;

/**
 * Builds the logic-only Artemis world used by {@link MapScreen}.
 */
public final class LogicWorldBuilder {
    private LogicWorldBuilder() {
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final GraphicsSettings graphics
    ) {
        return baseBuilder(client, stage, keyBindings, new ResourceData(), graphics);
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final ResourceData playerResources,
            final GraphicsSettings graphics
    ) {
        MapState state = MapState.builder()
                .playerResources(playerResources)
                .build();
        return createBuilder(client, stage, keyBindings, null, state, graphics, null);
    }

    public static WorldConfigurationBuilder builder(
            final MapStateProvider provider,
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final GraphicsSettings graphics,
            final java.util.function.Consumer<Float> callback
    ) {
        return createBuilder(client, stage, keyBindings, provider, new MapState(), graphics, callback);
    }

    public static WorldConfigurationBuilder builder(
            final MapState state,
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final GraphicsSettings graphics,
            final java.util.function.Consumer<Float> callback
    ) {
        return createBuilder(client, stage, keyBindings, new ProvidedMapStateProvider(state), state, graphics, callback);
    }

    private static WorldConfigurationBuilder createBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final MapStateProvider provider,
            final MapState state,
            final GraphicsSettings graphics,
            final java.util.function.Consumer<Float> callback
    ) {
        CameraInputSystem cameraInputSystem = new CameraInputSystem(client, keyBindings);
        cameraInputSystem.addProcessor(stage);
        SelectionSystem selectionSystem = new SelectionSystem(client, keyBindings);
        BuildPlacementSystem buildPlacementSystem = new BuildPlacementSystem(client, keyBindings);
        PlayerMovementSystem movementSystem = new PlayerMovementSystem(client, keyBindings);

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        cameraInputSystem,
                        selectionSystem,
                        buildPlacementSystem,
                        movementSystem,
                        new TileUpdateSystem(client),
                        new BuildingUpdateSystem(client),
                        new ResourceUpdateSystem(client),
                        new ChunkLoadSystem(client),
                        new ChunkRequestQueueSystem(client)
                );
        if (provider != null) {
            builder.with(new MapInitSystem(provider, true, callback));
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
        return world;
    }
}
