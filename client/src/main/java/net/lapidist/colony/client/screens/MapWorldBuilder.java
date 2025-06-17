package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.SpriteMapRendererFactory;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.client.graphics.ShaderPluginLoader;
import net.lapidist.colony.client.graphics.ShaderPlugin;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.ParticleSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.UISystem;
import net.lapidist.colony.client.systems.ChunkLoadSystem;
import net.lapidist.colony.client.systems.network.ChunkRequestQueueSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.CelestialSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.entities.CelestialBodyComponent;
import net.lapidist.colony.components.state.CameraPosition;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.client.entities.PlayerFactory;
import net.lapidist.colony.client.systems.SeasonCycleSystem;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.settings.KeyBindings;
import net.mostlyoriginal.api.event.common.EventSystem;

/**
 * Utility for constructing the {@link World} instance used by {@link MapScreen}.
 * <p>
 * The builder methods create {@link WorldConfigurationBuilder} instances pre
 * configured with all client systems required for the main map screen.
 */
public final class MapWorldBuilder {

    private MapWorldBuilder() {
    }

    private static final float HALF_ROTATION = 180f;

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
            final KeyBindings keyBindings,
            final GraphicsSettings graphics
    ) {
        return baseBuilder(client, stage, keyBindings, new ResourceData(), graphics);
    }

    /**
     * Variant of {@link #baseBuilder(GameClient, Stage, KeyBindings, GraphicsSettings)} that also
     * specifies the initial player resources.
     *
     * @param client          game client for network updates
     * @param stage           stage used by the UI system
     * @param keyBindings     input bindings for the player
     * @param playerResources resources available to the player at start
     * @return configured builder instance
     */
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
        return createBuilder(client, stage, keyBindings, null, state, graphics);
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
            final KeyBindings keyBindings,
            final GraphicsSettings graphics
    ) {
        return createBuilder(client, stage, keyBindings, provider, new MapState(), graphics);
    }

    /**
     * Convenience overload using a pre-built {@link MapState}.
     */
    public static WorldConfigurationBuilder builder(
            final MapState state,
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final GraphicsSettings graphics
    ) {
        return createBuilder(
                client,
                stage,
                keyBindings,
                new ProvidedMapStateProvider(state),
                state,
                graphics
        );
    }

    private static WorldConfigurationBuilder createBuilder(
            final GameClient client,
            final Stage stage,
            final KeyBindings keyBindings,
            final MapStateProvider provider,
            final MapState state,
            final GraphicsSettings graphics
    ) {
        ResourceData playerResources = state.playerResources();
        PlayerPosition playerPos = state.playerPos();
        CameraPosition cameraPos = state.cameraPos();
        MutableEnvironmentState environment = new MutableEnvironmentState(state.environment());
        CameraInputSystem cameraInputSystem = new CameraInputSystem(client, keyBindings);
        cameraInputSystem.addProcessor(stage);
        SelectionSystem selectionSystem = new SelectionSystem(client, keyBindings);
        BuildPlacementSystem buildPlacementSystem = new BuildPlacementSystem(client, keyBindings);
        ParticleSystem particleSystem = new ParticleSystem(new com.badlogic.gdx.graphics.g2d.SpriteBatch());
        PlayerMovementSystem movementSystem = new PlayerMovementSystem(client, keyBindings);

        ClearScreenSystem clear = new ClearScreenSystem(Color.BLACK);
        int rays = graphics != null ? graphics.getLightRays() : LightingSystem.DEFAULT_RAYS;
        boolean lightingEnabled = graphics != null && graphics.isLightingEnabled();
        boolean dayNightEnabled = graphics == null
                || (graphics.isLightingEnabled() && graphics.isDayNightCycleEnabled());
        LightingSystem lighting = null;
        if (lightingEnabled || dayNightEnabled) {
            lighting = new LightingSystem(clear, rays);
        }
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(
                        new EventSystem(),
                        clear,
                        cameraInputSystem,
                        selectionSystem,
                        buildPlacementSystem,
                        movementSystem,
                        new TileUpdateSystem(client),
                        new BuildingUpdateSystem(client),
                        new ResourceUpdateSystem(client),
                        new ChunkLoadSystem(client),
                        new ChunkRequestQueueSystem(client),
                        new CelestialSystem(client, environment),
                        new MapRenderSystem(),
                        particleSystem,
                        new UISystem(stage)
                );
        if (lighting != null) {
            builder.with(lighting);
        }
        if (dayNightEnabled) {
            builder.with(new SeasonCycleSystem(environment));
        }

        if (provider != null) {
            builder.with(
                    new MapInitSystem(provider),
                    new MapRenderDataSystem(client)
            );
        }

        return builder;
    }

    /**
     * Build a world using the supplied configuration.
     *
     * @param builder         preconfigured world builder with all desired systems
     * @param factory         optional factory for creating the map renderer
     * @param settings        user settings for renderer and graphics options
     * @param cameraPos       initial camera position or {@code null} for default
     * @param client          game client for map size information
     * @param playerResources starting resources for the player
     * @param playerPos       initial player tile position
     * @return configured world instance
     */
    public static World build(
            final WorldConfigurationBuilder builder,
            final MapRendererFactory factory,
            final Settings settings,
            final CameraPosition cameraPos,
            final GameClient client,
            final ResourceData playerResources,
            final PlayerPosition playerPos
    ) {
        MapRendererFactory actualFactory = factory;
        if (actualFactory == null) {
            actualFactory = new SpriteMapRendererFactory();
        }
        builder.with(new PlayerCameraSystem());

        ShaderPlugin plugin = null;
        if (settings != null) {
            String id = settings.getGraphicsSettings().getShaderPlugin();
            if (!"none".equals(id)) {
                for (ShaderPlugin p : new ShaderPluginLoader().loadPlugins()) {
                    if (p.id().equals(id)) {
                        plugin = p;
                        break;
                    }
                }
            }
        }

        World world = new World(builder.build());
        MapRenderSystem renderSystem = world.getSystem(MapRenderSystem.class);
        if (renderSystem != null) {
            MapRenderer renderer = actualFactory.create(world, plugin);
            renderSystem.setMapRenderer(renderer);
            renderSystem.setCameraProvider(world.getSystem(PlayerCameraSystem.class));
        }
        LightingSystem lightingSystem = world.getSystem(LightingSystem.class);
        if (plugin instanceof net.lapidist.colony.client.graphics.LightsNormalMapShaderPlugin ln
                && lightingSystem != null) {
            ln.setLightingSystem(lightingSystem);
        }
        if (lightingSystem != null && plugin instanceof net.lapidist.colony.client.graphics.LightingPlugin lp) {
            if (settings == null || settings.getGraphicsSettings().isLightingEnabled()) {
                lightingSystem.setRayHandler(lp.getRayHandler());
            }
        }
        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        if (cameraSystem != null && cameraPos != null) {
            cameraSystem.getCamera().position.set(cameraPos.x(), cameraPos.y(), 0);
            cameraSystem.getCamera().update();
        }
        PlayerFactory.create(world, client, playerResources, playerPos);
        Events.init(world.getSystem(EventSystem.class));
        createCelestialEntities(world);
        return world;
    }

    private static void createCelestialEntities(final World world) {
        float radius = 0f; // auto-calculated by system when zero
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
