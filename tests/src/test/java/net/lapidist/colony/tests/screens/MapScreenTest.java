package net.lapidist.colony.tests.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MapScreenEventHandler;
import net.lapidist.colony.client.screens.MapUi;
import net.lapidist.colony.client.screens.MapUiBuilder;
import net.lapidist.colony.client.screens.MapWorldBuilder;
import net.lapidist.colony.client.screens.LogicWorldBuilder;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class MapScreenTest {
    private static final float DELTA = 0.5f;
    private static final float HALF = 0.5f;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float SCALE = 1.5f;

    private static Stage extractStage(final MapScreen screen) throws Exception {
        Field stageField = MapScreen.class.getDeclaredField("stage");
        stageField.setAccessible(true);
        return (Stage) stageField.get(screen);
    }

    @Test
    public void delegatesLifecycleToHandlerAndProcessesWorld() throws Exception {
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);
        World logic = mock(World.class);
        World world = mock(World.class);
        MinimapActor minimap = mock(MinimapActor.class);

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<LogicWorldBuilder> logicStatic = mockStatic(LogicWorldBuilder.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            logicStatic.when(() -> LogicWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    eq(settings.getKeyBindings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            logicStatic.when(() -> LogicWorldBuilder.build(
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(logic);
            worldStatic.when(() -> MapWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    any(Stage.class),
                    eq(settings.getKeyBindings()),
                    eq(settings.getGraphicsSettings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(
                    any(),
                    isNull(),
                    eq(settings),
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(world);
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), eq(logic), eq(client), eq(colony), any()))
                    .thenAnswer(inv -> new MapUi(
                            inv.getArgument(0),
                            minimap,
                            mock(net.lapidist.colony.client.ui.ChatBox.class)
                    ));

            MapScreen screen = new MapScreen(colony, state, client);
            MapScreenEventHandler handler = mock(MapScreenEventHandler.class);
            Field f = MapScreen.class.getDeclaredField("events");
            f.setAccessible(true);
            f.set(screen, handler);

            screen.render(DELTA);
            screen.resize(WIDTH, HEIGHT);
            screen.pause();
            screen.resume();
            screen.hide();
            screen.show();
            screen.dispose();

            Field stepField = MapScreen.class.getDeclaredField("STEP_TIME");
            stepField.setAccessible(true);
            double step = stepField.getDouble(null);
            int expectedSteps = (int) Math.round(DELTA / step);

            verify(handler).update();
            verify(logic, times(expectedSteps)).setDelta((float) step);
            verify(logic, times(expectedSteps)).process();
            verify(world).setDelta(DELTA);
            verify(world).process();
            verify(handler).resize(WIDTH, HEIGHT);
            verify(handler).pause();
            verify(handler).resume();
            verify(handler).hide();
            verify(handler).show();
            verify(handler).dispose();
            verify(logic).dispose();
            verify(world).dispose();
            verify(minimap).dispose();
        }
    }

    @Test
    public void viewportUsesUiScale() throws Exception {
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        settings.setUiScale(SCALE);
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<LogicWorldBuilder> logicStatic = mockStatic(LogicWorldBuilder.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            logicStatic.when(() -> LogicWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    eq(settings.getKeyBindings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            logicStatic.when(() -> LogicWorldBuilder.build(
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(new World(new WorldConfigurationBuilder().build()));
            worldStatic.when(() -> MapWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    any(Stage.class),
                    eq(settings.getKeyBindings()),
                    eq(settings.getGraphicsSettings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(
                    any(),
                    isNull(),
                    eq(settings),
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(new World(new WorldConfigurationBuilder().build()));
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), any(World.class), eq(client), eq(colony), any()))
                    .thenAnswer(inv -> new MapUi(inv.getArgument(0), mock(MinimapActor.class),
                            mock(net.lapidist.colony.client.ui.ChatBox.class)));

            MapScreen screen = new MapScreen(colony, state, client);
            Stage stage = extractStage(screen);
            ScreenViewport vp = (ScreenViewport) stage.getViewport();
            assertEquals(1f / settings.getUiScale(), vp.getUnitsPerPixel(), 0f);
        }
    }

    @Test
    public void pausedSkipsWorldProcessing() throws Exception {
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);
        World logic = mock(World.class);
        World world = mock(World.class);

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<LogicWorldBuilder> logicStatic = mockStatic(LogicWorldBuilder.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            logicStatic.when(() -> LogicWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    eq(settings.getKeyBindings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            logicStatic.when(() -> LogicWorldBuilder.build(
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(logic);
            worldStatic.when(() -> MapWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    any(Stage.class),
                    eq(settings.getKeyBindings()),
                    eq(settings.getGraphicsSettings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(
                    any(),
                    isNull(),
                    eq(settings),
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(world);
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), eq(logic), eq(client), eq(colony), any()))
                    .thenAnswer(inv -> new MapUi(inv.getArgument(0), mock(MinimapActor.class),
                            mock(net.lapidist.colony.client.ui.ChatBox.class)));

            MapScreen screen = new MapScreen(colony, state, client);
            screen.setPaused(true);
            screen.render(DELTA);

            verify(logic, never()).process();
            verify(world, never()).process();
        }
    }

    @Test
    public void slowMotionUsesMultiplier() throws Exception {
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);
        World logic = mock(World.class);
        World world = mock(World.class);

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<LogicWorldBuilder> logicStatic = mockStatic(LogicWorldBuilder.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            logicStatic.when(() -> LogicWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    eq(settings.getKeyBindings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            logicStatic.when(() -> LogicWorldBuilder.build(
                    any(),
                    eq(client),
                    any(),
                    any()
            )).thenReturn(logic);
            worldStatic.when(() -> MapWorldBuilder.builder(
                    eq(state),
                    eq(client),
                    any(Stage.class),
                    eq(settings.getKeyBindings()),
                    eq(settings.getGraphicsSettings()),
                    any()
            )).thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(any(), isNull(), eq(settings), any(),
                    eq(client), any(), any()))
                    .thenReturn(world);
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), eq(logic), eq(client), eq(colony), any()))
                    .thenAnswer(inv -> new MapUi(inv.getArgument(0), mock(MinimapActor.class),
                            mock(net.lapidist.colony.client.ui.ChatBox.class)));

            MapScreen screen = new MapScreen(colony, state, client);
            Field stepField = MapScreen.class.getDeclaredField("STEP_TIME");
            stepField.setAccessible(true);
            double step = stepField.getDouble(null);
            int expectedSteps = (int) Math.round((DELTA * HALF) / step);

            screen.setSpeedMultiplier(HALF);
            screen.render(DELTA);

            verify(logic, times(expectedSteps)).process();
            verify(world).process();
        }
    }
}
