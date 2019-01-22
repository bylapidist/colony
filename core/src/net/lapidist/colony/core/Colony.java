package net.lapidist.colony.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.GameLoadEvent;

public class Colony extends Game {

    private static Colony instance;
    private static Batch spriteBatch;
    private static ShapeRenderer shapeBatch;
    private static AssetManager assetManager;
    private static InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        instance = this;
        spriteBatch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        inputMultiplexer = new InputMultiplexer();
        assetManager = new AssetManager();

        Gdx.input.setInputProcessor(inputMultiplexer);
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeBatch.dispose();
    }

    public static Batch getSpriteBatch() {
        return spriteBatch;
    }

    public static ShapeRenderer getShapeBatch() {
        return shapeBatch;
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public static Colony getInstance() {
        return instance;
    }
}
