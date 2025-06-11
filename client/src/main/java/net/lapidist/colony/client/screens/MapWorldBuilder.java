package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.SpriteMapRendererFactory;
import net.lapidist.colony.client.renderers.ModelBatchMapRendererFactory;
import net.lapidist.colony.client.systems.PerspectiveCameraSystem;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.UISystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.PlayerInitSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.settings.KeyBindings;
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
     * customise the returned builder before calling
     * the {@code build} method with a {@link MapRendererFactory} and
     * {@link net.lapidist.colony.settings.Settings}.
     *
     * @param client game client for network updates
     * @param stage  stage used by the UI system
     * @return configured builder instance
     */
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
        return createBuilder(client, stage, keyBindings, null, playerResources);
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
            final Stage stage,
            final KeyBindings keyBindings
    ) {
        return createBuilder(client, stage, keyBindings, provider, new ResourceData());
    }

    /**
     * Convenience overload using a pre-built {@link MapState}.
     */
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
                state.playerResources()
        );
    }

    private static WorldConfigurationBuilder createBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final MapStateProvider provider,
            final ResourceData playerResources
    ) {
        CameraInputSystem cameraInputSystem = new CameraInputSystem(keyBindings);
        cameraInputSystem.addProcessor(stage);
        SelectionSystem selectionSystem = new SelectionSystem(client, keyBindings);
        BuildPlacementSystem buildPlacementSystem = new BuildPlacementSystem(client, keyBindings);

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        new ClearScreenSystem(Color.BLACK),
                        cameraInputSystem,
                        selectionSystem,
                        buildPlacementSystem,
                        new PlayerInitSystem(playerResources),
                        new TileUpdateSystem(client),
                        new BuildingUpdateSystem(client),
                        new ResourceUpdateSystem(client),
                        new MapRenderSystem(),
                        new UISystem(stage)
                );

        if (provider != null) {
            builder.with(
                    new MapInitSystem(provider),
                    new MapRenderDataSystem()
            );
        }

        return builder;
    }

    /**
     * Build a world using the supplied configuration.
     *
     * @param builder preconfigured world builder with all desired systems
     * @param factory optional factory for creating the map renderer
     * @return configured world instance
     */
    public static World build(
            final WorldConfigurationBuilder builder,
            final MapRendererFactory factory,
            final Settings settings
    ) {
        MapRendererFactory actualFactory = factory;
        if (actualFactory == null) {
            String type = settings != null
                    ? settings.getGraphicsSettings().getRenderer()
                    : "sprite";
            actualFactory = "model".equals(type)
                    ? new ModelBatchMapRendererFactory()
                    : new SpriteMapRendererFactory();
        }
        if (actualFactory instanceof ModelBatchMapRendererFactory) {
            builder.with(new PerspectiveCameraSystem());
        } else {
            builder.with(new PlayerCameraSystem());
        }

        World world = new World(builder.build());
        MapRenderSystem renderSystem = world.getSystem(MapRenderSystem.class);
        if (renderSystem != null) {
            MapRenderer renderer = actualFactory.create(world);
            renderSystem.setMapRenderer(renderer);
            if (actualFactory instanceof ModelBatchMapRendererFactory) {
                renderSystem.setCameraProvider(world.getSystem(PerspectiveCameraSystem.class));
            } else {
                renderSystem.setCameraProvider(world.getSystem(PlayerCameraSystem.class));
            }
        }
        Events.init(world.getSystem(EventSystem.class));
        return world;
    }
}
