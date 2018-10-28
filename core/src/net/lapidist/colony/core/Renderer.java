package net.lapidist.colony.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitfire.postprocessing.PostProcessor;
import net.lapidist.colony.shaders.ShaderProvider;
import net.lapidist.colony.modules.RendererModule;
import net.lapidist.colony.systems.DebugRenderingSystem;
import net.lapidist.colony.systems.RenderingSystem;

import static net.lapidist.colony.core.Core.*;

import static net.lapidist.colony.Constants.*;

public class Renderer extends RendererModule {

    public Renderer() {
        Core.camera = new Camera(Camera.CameraState.STATIC);
        Core.decalBatch = new DecalBatch(new CameraGroupStrategy(Core.camera));
        Core.spriteBatch = new SpriteBatch();
        Core.shapeBatch = new ShapeRenderer();
        Core.modelBatch = new ModelBatch(new ShaderProvider());
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
