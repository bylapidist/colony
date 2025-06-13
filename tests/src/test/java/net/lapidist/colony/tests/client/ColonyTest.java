package net.lapidist.colony.tests.client;

import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.client.events.GameInitEvent;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.events.Events;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.i18n.I18n;
import org.mockito.MockedStatic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class ColonyTest {

    @Test
    public void startGameLaunchesServerAndClient() throws Exception {
        Colony colony = new Colony();
        try (MockedConstruction<GameServer> serverCons =
                     mockConstruction(GameServer.class);
             MockedConstruction<GameClient> clientCons =
                     mockConstruction(GameClient.class);
             MockedConstruction<com.badlogic.gdx.graphics.g2d.SpriteBatch> batchCons =
                     mockConstruction(com.badlogic.gdx.graphics.g2d.SpriteBatch.class)) {
            colony.startGame("test");

            GameServer server = serverCons.constructed().get(0);
            GameClient client = clientCons.constructed().get(0);
            verify(server).start();
            verify(client).start(any());
        }
    }

    @Test
    public void returnToMainMenuStopsServerAndClientAndShowsMenu() throws Exception {
        Colony colony = new Colony();
        try (MockedConstruction<GameServer> serverCons =
                     mockConstruction(GameServer.class);
             MockedConstruction<GameClient> clientCons =
                     mockConstruction(GameClient.class);
             MockedConstruction<MainMenuScreen> menuCons =
                     mockConstruction(MainMenuScreen.class);
             MockedConstruction<com.badlogic.gdx.graphics.g2d.SpriteBatch> batchCons =
                     mockConstruction(com.badlogic.gdx.graphics.g2d.SpriteBatch.class)) {
            colony.startGame("test");
            GameServer server = serverCons.constructed().get(0);
            GameClient client = clientCons.constructed().get(0);

            colony.returnToMainMenu();

            verify(client).stop();
            verify(server).stop();
            assertSame(menuCons.constructed().get(0), colony.getScreen());
        }
    }

    @Test
    public void createInitialisesSettingsAndDispatchesEvent() throws Exception {
        Colony colony = new Colony();
        try (MockedStatic<Paths> pathsStatic = mockStatic(Paths.class);
             MockedStatic<Settings> settingsStatic = mockStatic(Settings.class);
             MockedStatic<I18n> i18nStatic = mockStatic(I18n.class);
             MockedConstruction<ModLoader> loaderCons =
                     mockConstruction(ModLoader.class, (m, c) ->
                             when(m.loadMods()).thenReturn(java.util.List.of()));
             MockedConstruction<MainMenuScreen> menuCons = mockConstruction(MainMenuScreen.class)) {
            Settings settings = new Settings();
            settingsStatic.when(Settings::load).thenReturn(settings);
            Paths paths = mock(Paths.class);
            pathsStatic.when(Paths::get).thenReturn(paths);

            EventSystem system = new EventSystem();
            Events.init(system);
            final boolean[] initEvent = {false};
            system.registerEvents(new Object() {
                @Subscribe
                public void on(final GameInitEvent e) {
                    initEvent[0] = true;
                }
            });

            colony.create();

            settingsStatic.verify(Settings::load);
            verify(paths).createGameFoldersIfNotExists();
            i18nStatic.verify(() -> I18n.setLocale(settings.getLocale()));
            assertSame(settings, colony.getSettings());
            assertSame(menuCons.constructed().get(0), colony.getScreen());
            assertTrue(initEvent[0]);
        }
    }

    @Test
    public void disposeStopsActiveConnectionsAndEvents() throws Exception {
        Colony colony = new Colony();
        try (MockedConstruction<GameServer> serverCons =
                     mockConstruction(GameServer.class);
             MockedConstruction<GameClient> clientCons =
                     mockConstruction(GameClient.class);
             MockedConstruction<com.badlogic.gdx.graphics.g2d.SpriteBatch> batchCons =
                     mockConstruction(com.badlogic.gdx.graphics.g2d.SpriteBatch.class)) {
            colony.startGame("test");
            GameServer server = serverCons.constructed().get(0);
            GameClient client = clientCons.constructed().get(0);

            colony.dispose();

            verify(client).stop();
            verify(server).stop();
            assertNull(Events.getInstance());
        }
    }
}
