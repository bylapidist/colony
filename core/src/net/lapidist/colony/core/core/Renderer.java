package net.lapidist.colony.core.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.lapidist.colony.common.postprocessing.PostProcessor;
import net.lapidist.colony.core.modules.RendererModule;
import net.lapidist.colony.core.systems.DebugRenderingSystem;
import net.lapidist.colony.core.systems.RenderingSystem;

import static net.lapidist.colony.core.Constants.engine;
import static net.lapidist.colony.core.Constants.tweenManager;
import static net.lapidist.colony.core.core.Core.camera;

public class Renderer extends RendererModule {

    public Renderer() {
        Core.camera = new Camera(Camera.CameraState.STATIC);
        Core.spriteBatch = new SpriteBatch();
        Core.shapeBatch = new ShapeRenderer();
        Core.postProcessor = new PostProcessor(false, false, true);
    }

    @Override
    public void update() {
        camera.update();
        tweenManager.update(Gdx.graphics.getDeltaTime());
        draw();
    }

    @Override
    public void draw() {
        Graphics.clear(Color.BLACK);

        engine.getSystem(RenderingSystem.class).draw();
        engine.getSystem(DebugRenderingSystem.class).draw();
    }
}
