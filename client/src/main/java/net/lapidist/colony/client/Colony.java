package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.client.screens.LoadingScreen;
import net.lapidist.colony.client.screens.ErrorScreen;
import net.lapidist.colony.client.screens.NewGameScreen;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.client.events.GameInitEvent;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;

import java.io.IOException;

public final class Colony extends Game {

    private GameClient client;
    private GameServer server;
    private Settings settings;
    private java.util.List<LoadedMod> mods;

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

    /**
     * Starts or resumes a game using the dimensions stored in the autosave file.
     * Defaults to {@link net.lapidist.colony.components.state.MapState#DEFAULT_WIDTH}
     * and {@link net.lapidist.colony.components.state.MapState#DEFAULT_HEIGHT}
     * when no save exists.
     */
    public void startGame(final String saveName) {
        try {
            java.nio.file.Path file = Paths.get().getAutosave(saveName);
            if (java.nio.file.Files.exists(file)) {
                MapState state = GameStateIO.load(file);
                startGame(saveName, state.width(), state.height());
            } else {
                setScreen(new NewGameScreen(this));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame(final String saveName, final int width, final int height) {
        try {
            if (server != null) {
                server.stop();
            }
            server = new GameServer(
                    GameServerConfig.builder()
                            .saveName(saveName)
                            .width(width)
                            .height(height)
                            .build()
            );
            server.start();
            client = new GameClient();
            client.setConnectionErrorCallback(e -> Gdx.app.postRunnable(() -> {
                client.stop();
                if (server != null) {
                    server.stop();
                    server = null;
                }
                setScreen(new ErrorScreen(this, I18n.get("error.connectionFailed")));
            }));
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
            mods = new java.util.ArrayList<>();
            for (net.lapidist.colony.mod.GameMod builtin : java.util.ServiceLoader.load(net.lapidist.colony.mod.GameMod.class)) {
                mods.add(new LoadedMod(builtin, builtinMetadata(builtin.getClass())));
            }
            mods.addAll(new ModLoader(Paths.get()).loadMods());
            for (LoadedMod mod : mods) {
                mod.mod().init();
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
            for (LoadedMod mod : mods) {
                mod.mod().dispose();
            }
        }
        Events.dispose();
    }

    private static net.lapidist.colony.mod.ModMetadata builtinMetadata(final Class<?> cls) {
        String id;
        if (cls.getName().equals("net.lapidist.colony.base.BaseMapServiceMod")) {
            id = "base-map-service";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseNetworkMod")) {
            id = "base-network";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseAutosaveMod")) {
            id = "base-autosave";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseResourceProductionMod")) {
            id = "base-resource-production";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseHandlersMod")) {
            id = "base-handlers";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseDefinitionsMod")) {
            id = "base-definitions";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseResourcesMod")) {
            id = "base-resources";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseCommandBusMod")) {
            id = "base-command-bus";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseMapGenerationMod")) {
            id = "base-map-generation";
        } else if (cls.getName().equals("net.lapidist.colony.base.BaseGameplaySystemsMod")) {
            id = "base-systems";
        } else {
            id = cls.getSimpleName();
        }
        return new net.lapidist.colony.mod.ModMetadata(id, "1.0.0", java.util.List.of());
    }
}
