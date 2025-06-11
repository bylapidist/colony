package net.lapidist.colony.tests.client;

import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.tests.GdxTestRunner;
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
        try (MockedConstruction<GameServer> serverCons = mockConstruction(GameServer.class);
             MockedConstruction<GameClient> clientCons = mockConstruction(GameClient.class)) {
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
        try (MockedConstruction<GameServer> serverCons = mockConstruction(GameServer.class);
             MockedConstruction<GameClient> clientCons = mockConstruction(GameClient.class);
             MockedConstruction<MainMenuScreen> menuCons = mockConstruction(MainMenuScreen.class)) {
            colony.startGame("test");
            GameServer server = serverCons.constructed().get(0);
            GameClient client = clientCons.constructed().get(0);

            colony.returnToMainMenu();

            verify(client).stop();
            verify(server).stop();
            assertSame(menuCons.constructed().get(0), colony.getScreen());
        }
    }
}
