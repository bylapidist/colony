package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Very basic 3-D implementation of {@link MapRenderer} for experiments.
 */
public final class ModelBatchMapRenderer implements MapRenderer, Disposable {

    private final ModelBatch modelBatch;
    private final ModelInstance tileModel;

    public ModelBatchMapRenderer(final ModelBatch batch, final ModelInstance tileModelToSet) {
        this.modelBatch = batch;
        this.tileModel = tileModelToSet;
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider camera) {
        modelBatch.begin(camera.getCamera());
        for (int i = 0; i < map.getTiles().size; i++) {
            modelBatch.render(tileModel);
        }
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
