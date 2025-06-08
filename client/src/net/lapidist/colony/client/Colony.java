package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.client.events.GameInitEvent;

import java.io.IOException;

public final class Colony extends Game {

    private GameClient client;
    private GameServer server;
    private Settings settings;

    public void returnToMainMenu() {
        if (client != null) {
            client.stop();
            client = null;
        }
        if (server != null) {
            server.stop();
            server = null;
        }
        setScreen(new MainMenuScreen(this));
    }

    public void startGame(final String saveName) {
        MapState state;
        try {
            if (server != null) {
                server.stop();
            }
            server = new GameServer(
                    GameServerConfig.builder().saveName(saveName).build()
            );
            server.start();
            client = new GameClient();
            client.start();
            state = client.getMapState();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        setScreen(new MapScreen(this, state, client));
    }

    public void startGame() {
        startGame(ColonyConfig.get().getString("game.defaultSaveName"));
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void create() {
        // Do global initialisation
        try {
            Paths.createGameFoldersIfNotExists();
            settings = Settings.load();
            I18n.setLocale(settings.getLocale());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Dispatch events
        Events.dispatch(new GameInitEvent());

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        if (client != null) {
            client.stop();
        }
        if (server != null) {
            server.stop();
        }
        Events.dispose();
    }
}
