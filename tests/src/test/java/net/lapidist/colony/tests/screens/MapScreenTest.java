package net.lapidist.colony.tests.screens;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MapScreenEventHandler;
import net.lapidist.colony.client.screens.MapUi;
import net.lapidist.colony.client.screens.MapUiBuilder;
import net.lapidist.colony.client.screens.MapWorldBuilder;
import net.lapidist.colony.client.ui.MinimapActor;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MapScreenTest {
    private static final float DELTA = 0.5f;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Test
    public void delegatesLifecycleToHandlerAndProcessesWorld() throws Exception {
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapState state = new MapState();
        GameClient client = mock(GameClient.class);
        World world = mock(World.class);
        MinimapActor minimap = mock(MinimapActor.class);

        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class);
             MockedStatic<MapWorldBuilder> worldStatic = mockStatic(MapWorldBuilder.class);
             MockedStatic<MapUiBuilder> uiStatic = mockStatic(MapUiBuilder.class)) {
            worldStatic.when(() -> MapWorldBuilder.builder(eq(state), eq(client),
                    any(Stage.class), eq(settings.getKeyBindings())))
                    .thenReturn(new WorldConfigurationBuilder());
            worldStatic.when(() -> MapWorldBuilder.build(any(), isNull(), eq(settings)))
                    .thenReturn(world);
            uiStatic.when(() -> MapUiBuilder.build(any(Stage.class), eq(world), eq(client), eq(colony)))
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

            verify(handler).update();
            verify(world).setDelta(DELTA);
            verify(world).process();
            verify(handler).resize(WIDTH, HEIGHT);
            verify(handler).pause();
            verify(handler).resume();
            verify(handler).hide();
            verify(handler).show();
            verify(handler).dispose();
            verify(world).dispose();
            verify(minimap).dispose();
        }
    }
}
