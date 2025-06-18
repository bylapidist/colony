package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.entities.PlayerFactory;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.CelestialSystem;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.network.ChunkRequestQueueSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.components.entities.CelestialBodyComponent;
import net.lapidist.colony.components.state.CameraPosition;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.settings.KeyBindings;
import net.mostlyoriginal.api.event.common.EventSystem;

/**
 * Builds the update-only world for {@link MapScreen}.
 */
public final class LogicWorldBuilder {

    private static final float HALF_ROTATION = 180f;

    private LogicWorldBuilder() {
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings
    ) {
        return baseBuilder(client, stage, keyBindings, new ResourceData());
    }

    public static WorldConfigurationBuilder baseBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final ResourceData playerResources
    ) {
        MapState state = MapState.builder()
                .playerResources(playerResources)
                .build();
        return createBuilder(client, stage, keyBindings, null, state);
    }

    public static WorldConfigurationBuilder builder(
            final MapStateProvider provider,
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings
    ) {
        return createBuilder(client, stage, keyBindings, provider, new MapState());
    }

    public static WorldConfigurationBuilder builder(
            final MapState state,
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings
    ) {
        return createBuilder(
                client,
                stage,
                keyBindings,
                new ProvidedMapStateProvider(state),
                state
        );
    }

    private static WorldConfigurationBuilder createBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final MapStateProvider provider,
            final MapState state
    ) {
        MutableEnvironmentState environment = new MutableEnvironmentState(state.environment());
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
                        new ChunkRequestQueueSystem(client),
                        new CelestialSystem(client, environment)
                );

        if (provider != null) {
            builder.with(new MapInitSystem(provider));
        }

        return builder;
    }

    public static World build(
            final WorldConfigurationBuilder builder,
            final GameClient client,
            final CameraPosition cameraPos,
            final ResourceData playerResources,
            final PlayerPosition playerPos
    ) {
        builder.with(new PlayerCameraSystem());
        World world = new World(builder.build());
        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        if (cameraSystem != null && cameraPos != null) {
            cameraSystem.setTargetPosition(new com.badlogic.gdx.math.Vector2(cameraPos.x(), cameraPos.y()));
            cameraSystem.getCamera().position.set(cameraPos.x(), cameraPos.y(), 0);
            cameraSystem.getCamera().update();
        }
        PlayerFactory.create(world, client, playerResources, playerPos);
        Events.init(world.getSystem(EventSystem.class));
        createCelestialEntities(world);
        return world;
    }

    private static void createCelestialEntities(final World world) {
        float radius = 0f;
        var sun = world.createEntity();
        var sunComp = new CelestialBodyComponent();
        sunComp.setTexture("player0");
        sunComp.setOrbitRadius(radius);
        sunComp.setOrbitOffset(0f);
        sun.edit().add(sunComp);

        var moon = world.createEntity();
        var moonComp = new CelestialBodyComponent();
        moonComp.setTexture("player0");
        moonComp.setOrbitRadius(radius);
        moonComp.setOrbitOffset(HALF_ROTATION);
        moon.edit().add(moonComp);
    }
}
