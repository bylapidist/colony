package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.client.screens.LoadingScreen;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.client.events.GameInitEvent;
import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.ModLoader;

import java.io.IOException;

public final class Colony extends Game {

    private GameClient client;
    private GameServer server;
    private Settings settings;
    private java.util.List<GameMod> mods;

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
        try {
            if (server != null) {
                server.stop();
            }
            server = new GameServer(
                    GameServerConfig.builder().saveName(saveName).build()
            );
            server.start();
            client = new GameClient();
            LoadingScreen loading = new LoadingScreen();
            loading.setMessage(I18n.get("loading.connect"));
            client.setLoadProgressListener(p -> Gdx.app.postRunnable(() -> loading.setProgress(p)));
            client.setLoadMessageListener(msg -> Gdx.app.postRunnable(() -> loading.setMessage(msg)));
            setScreen(loading);
            client.start(state ->
                    Gdx.app.postRunnable(() -> setScreen(new MapScreen(this, state, client))));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            Paths.get().createGameFoldersIfNotExists();
            settings = Settings.load();
            I18n.setLocale(settings.getLocale());
            mods = new ModLoader(Paths.get()).loadMods();
            for (GameMod mod : mods) {
                mod.init();
            }
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
        if (mods != null) {
            for (GameMod mod : mods) {
                mod.dispose();
            }
        }
        Events.dispose();
    }
}
