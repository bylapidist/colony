package net.lapidist.colony.tests.screens;

import com.artemis.Aspect;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapWorldBuilder;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.PlayerMovementSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.SeasonCycleSystem;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.PlayerPosition;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MapWorldBuilderConfigurationTest {

    private static final int WOOD = 7;
    private static final int STONE = 3;
    private static final int FOOD = 2;


    @Test
    public void baseBuilderRegistersDefaultSystems() {
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.baseBuilder(client, stage, keys, settings.getGraphicsSettings()),
                    null,
                    settings,
                    null,
                    client,
                    new ResourceData(),
                    null
            );

            assertNotNull(world.getSystem(CameraInputSystem.class));
            assertNotNull(world.getSystem(SelectionSystem.class));
            assertNotNull(world.getSystem(BuildPlacementSystem.class));
            assertNotNull(world.getSystem(PlayerMovementSystem.class));
            assertNotNull(world.getSystem(MapRenderSystem.class));
            assertNull(world.getSystem(MapInitSystem.class));
            assertNull(world.getSystem(MapRenderDataSystem.class));
            assertNotNull(world.getSystem(LightingSystem.class));
            assertNotNull(world.getSystem(net.lapidist.colony.client.systems.CelestialSystem.class));
            assertNotNull(world.getSystem(SeasonCycleSystem.class));

            world.dispose();
        }
    }

    @Test
    public void builderRegistersMapSystems() {
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.builder(
                            new ProvidedMapStateProvider(state),
                            client,
                            stage,
                            keys,
                            settings.getGraphicsSettings(),
                            null
                    ),
                    null,
                    settings,
                    null,
                    client,
                    state.playerResources(),
                    state.playerPos()
            );
            world.process();

            assertNotNull(world.getSystem(MapInitSystem.class));
            assertNotNull(world.getSystem(PlayerCameraSystem.class));
            assertNotNull(world.getSystem(PlayerMovementSystem.class));
            assertNotNull(world.getSystem(MapRenderDataSystem.class));
            assertNotNull(world.getSystem(net.lapidist.colony.client.systems.CelestialSystem.class));
            assertNotNull(world.getSystem(SeasonCycleSystem.class));
            assertEquals(1, world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerResourceComponent.class))
                    .getEntities().size());

            world.dispose();
        }
    }

    @Test
    public void builderFromStateUsesInitialResources() {
        ResourceData resources = new ResourceData(WOOD, STONE, FOOD);
        MapState state = MapState.builder()
                .playerResources(resources)
                .build();
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.builder(state, client, stage, keys, settings.getGraphicsSettings()),
                    null,
                    settings,
                    null,
                    client,
                    state.playerResources(),
                    state.playerPos()
            );
            PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
            camera.toggleMode();
            world.process();

            var players = world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerResourceComponent.class))
                    .getEntities();
            var comp = world.getMapper(PlayerResourceComponent.class)
                    .get(world.getEntity(players.get(0)));
            assertEquals(WOOD, comp.getWood());
            assertEquals(STONE, comp.getStone());
            assertEquals(FOOD, comp.getFood());

            world.dispose();
        }
    }

    @Test
    public void baseBuilderUsesProvidedResources() {
        ResourceData resources = new ResourceData(WOOD, STONE, FOOD);
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.baseBuilder(client, stage, keys, resources, settings.getGraphicsSettings()),
                    null,
                    settings,
                    null,
                    client,
                    resources,
                    null
            );
            world.process();

            var players = world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerResourceComponent.class))
                    .getEntities();
            var comp = world.getMapper(PlayerResourceComponent.class)
                    .get(world.getEntity(players.get(0)));
            assertEquals(WOOD, comp.getWood());
            assertEquals(STONE, comp.getStone());
            assertEquals(FOOD, comp.getFood());

            world.dispose();
        }
    }

    @Test
    public void builderFromStateUsesPlayerPosition() {
        final int x = 2;
        final int y = 3;
        PlayerPosition pos = new PlayerPosition(x, y);
        MapState state = MapState.builder()
                .playerPos(pos)
                .build();
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.builder(state, client, stage, keys, settings.getGraphicsSettings()),
                    null,
                    settings,
                    null,
                    client,
                    state.playerResources(),
                    state.playerPos()
            );
            PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
            camera.toggleMode();
            world.process();

            var players = world.getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerComponent.class))
                    .getEntities();
            var pc = world.getMapper(PlayerComponent.class)
                    .get(world.getEntity(players.get(0)));
            final float epsilon = 0.01f;
            assertEquals(x * GameConstants.TILE_SIZE, pc.getX(), epsilon);
            assertEquals(y * GameConstants.TILE_SIZE, pc.getY(), epsilon);

            world.dispose();
        }
    }

    @Test
    public void selectsCameraSystemFromSettings() {
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        settings.getGraphicsSettings().setRenderer("sprite");
        World world = MapWorldBuilder.build(
                new com.artemis.WorldConfigurationBuilder(),
                null,
                settings,
                null,
                null,
                new ResourceData(),
                null
        );
        assertNotNull(world.getSystem(PlayerCameraSystem.class));
        world.dispose();
    }

    @Test
    public void disablesDayNightWhenLightingOff() {
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        net.lapidist.colony.settings.Settings settings = new net.lapidist.colony.settings.Settings();
        settings.getGraphicsSettings().setLightingEnabled(false);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.baseBuilder(client, stage, keys, settings.getGraphicsSettings()),
                    null,
                    settings,
                    null,
                    client,
                    new ResourceData(),
                    null
            );
            assertNull(world.getSystem(LightingSystem.class));
            world.dispose();
        }
    }
}
