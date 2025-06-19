package net.lapidist.colony.tests.async;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MapUi;
import net.lapidist.colony.client.screens.MapUiBuilder;
import net.lapidist.colony.client.screens.MapWorldBuilder;
import net.lapidist.colony.client.screens.LogicWorldBuilder;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MapScreenAsyncTest {

    private static final int ITERATIONS = 10;
    private static final float STEP = 0.1f;
    private static final int SLEEP_MS = 10;

    @Test
    public void processesUntilMapReady() throws Exception {
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);
        AtomicBoolean called = new AtomicBoolean(false);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<LogicWorldBuilder> logicStatic = mockStatic(LogicWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            World logicWorld = new World(new WorldConfigurationBuilder()
                    .with(new MapInitSystem(new ProvidedMapStateProvider(state), true, p -> {
                        if (p == 1f) {
                            called.set(true);
                        }
                    })).build());
            World renderWorld = new World(new WorldConfigurationBuilder().build());
            logicStatic.when(() -> LogicWorldBuilder.builder(eq(state), eq(client), any(Stage.class),
                    eq(settings.getKeyBindings()), eq(settings.getGraphicsSettings()), any()))
                    .thenReturn(new WorldConfigurationBuilder());
            logicStatic.when(() -> LogicWorldBuilder.build(any(), eq(client), any(), any()))
                    .thenReturn(logicWorld);
            worldStatic.when(() -> MapWorldBuilder.builder(eq(state), eq(client), any(Stage.class),
                    eq(settings.getKeyBindings()), eq(settings.getGraphicsSettings()), any()))
                    .thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(any(), isNull(), eq(settings), any(),
                    eq(client), any(), any()))
                    .thenReturn(renderWorld);
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), eq(logicWorld), eq(client), eq(colony)))
                    .thenAnswer(inv -> new MapUi(inv.getArgument(0), mock(MinimapActor.class),
                            mock(net.lapidist.colony.client.ui.ChatBox.class)));

            MapScreen screen = new MapScreen(colony, state, client, p -> { });
            for (int i = 0; i < ITERATIONS && !called.get(); i++) {
                screen.render(STEP);
                Thread.sleep(SLEEP_MS);
            }
            screen.render(STEP);
        }
    }
}
