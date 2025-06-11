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
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.PlayerInitSystem;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
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
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.baseBuilder(client, stage, keys),
                    null,
                    new net.lapidist.colony.settings.Settings()
            );

            assertNotNull(world.getSystem(CameraInputSystem.class));
            assertNotNull(world.getSystem(SelectionSystem.class));
            assertNotNull(world.getSystem(BuildPlacementSystem.class));
            assertNotNull(world.getSystem(PlayerInitSystem.class));
            assertNotNull(world.getSystem(MapRenderSystem.class));
            assertNull(world.getSystem(MapInitSystem.class));
            assertNull(world.getSystem(MapRenderDataSystem.class));

            world.dispose();
        }
    }

    @Test
    public void builderRegistersMapSystems() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        Batch batch = mock(Batch.class);
        when(batch.getTransformMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        when(batch.getProjectionMatrix()).thenReturn(new com.badlogic.gdx.math.Matrix4());
        Stage stage = new Stage(new ScreenViewport(), batch);
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.builder(new ProvidedMapStateProvider(state), client, stage, keys),
                    null,
                    new net.lapidist.colony.settings.Settings()
            );
            world.process();

            assertNotNull(world.getSystem(MapInitSystem.class));
            assertNotNull(world.getSystem(PlayerCameraSystem.class));
            assertNotNull(world.getSystem(MapRenderDataSystem.class));
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
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            World world = MapWorldBuilder.build(
                    MapWorldBuilder.builder(state, client, stage, keys),
                    null,
                    new net.lapidist.colony.settings.Settings()
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
}
