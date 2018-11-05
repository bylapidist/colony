package net.lapidist.colony.core.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitfire.postprocessing.PostProcessor;

import static net.lapidist.colony.core.Constants.*;

public class Core {

    public static Camera camera;
    public static DecalBatch decalBatch;
    public static Batch spriteBatch;
    public static ShapeRenderer shapeBatch;
    public static ModelBatch modelBatch;
    public static PostProcessor postProcessor;

    public static void dispose() {
        // Dispose of global static resources here

        if (decalBatch != null) {
            decalBatch.dispose();
            decalBatch = null;
        }

        if (spriteBatch != null) {
            spriteBatch.dispose();
            spriteBatch = null;
        }

        if (shapeBatch != null) {
            shapeBatch.dispose();
            shapeBatch = null;
        }

        if (resourceLoader != null) {
            resourceLoader.dispose();
            resourceLoader = null;
        }

        if (tweenManager != null) {
            tweenManager.killAll();
            tweenManager = null;
        }

        if (engine != null) {
            engine.clearPools();
            engine = null;
        }

        if (logic != null) {
            logic.dispose();
            logic = null;
        }

        if (world != null) {
            world.dispose();
            world = null;
        }

        if (control != null) {
            control.dispose();
            control = null;
        }

        if (ui != null) {
            ui.dispose();
            ui = null;
        }

        if (renderer != null) {
            renderer.dispose();
            renderer = null;
        }

        if (postProcessor != null) {
            postProcessor.dispose();
            postProcessor = null;
        }
    }
}
