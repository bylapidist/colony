package net.lapidist.colony.core;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.io.FileLocation;
import net.lapidist.colony.common.io.ResourceLoader;
import net.lapidist.colony.common.postprocessing.PostProcessor;
import net.lapidist.colony.common.utils.ShaderLoader;
import net.lapidist.colony.core.events.GameLoadEvent;
import net.lapidist.colony.core.tween.Accessors;

import java.io.IOException;

public class Colony extends Game {

    private static Colony instance;
    private static Batch spriteBatch;
    private static ShapeRenderer shapeBatch;
    private static PostProcessor postProcessor;
    private static TweenManager tweenManager;
    private static ResourceLoader resourceLoader;
    private static InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        ShaderLoader.BasePath = "shaders/postprocessing/";
        Accessors.register();

        instance = this;
        spriteBatch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        postProcessor = new PostProcessor(false, false, true);
        tweenManager = new TweenManager();
        inputMultiplexer = new InputMultiplexer();

        try {
            resourceLoader = new ResourceLoader(
                    FileLocation.INTERNAL,
                    FileLocation.INTERNAL.getFile("resources.xml")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void render() {
        super.render();

        // @TODO: move to TweenSystem
        tweenManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();

        spriteBatch.dispose();
        shapeBatch.dispose();
        postProcessor.dispose();
    }

    public static Batch getSpriteBatch() {
        return spriteBatch;
    }

    public static ShapeRenderer getShapeBatch() {
        return shapeBatch;
    }

    public static PostProcessor getPostProcessor() {
        return postProcessor;
    }

    public static TweenManager getTweenManager() {
        return tweenManager;
    }

    public static ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public static InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public static Colony getInstance() {
        return instance;
    }
}
