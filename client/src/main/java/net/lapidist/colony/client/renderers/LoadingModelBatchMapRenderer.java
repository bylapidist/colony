package net.lapidist.colony.client.renderers;

import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;

import java.util.function.Consumer;

/**
 * ModelBatch renderer that loads resources asynchronously.
 */
public final class LoadingModelBatchMapRenderer implements MapRenderer, Disposable {

    private final World world;
    private final ResourceLoader loader;
    private final String modelPath;
    private final Consumer<Float> progressCallback;
    private final ModelBatch batch = new ModelBatch();

    private ModelBatchMapRenderer delegate;

    public LoadingModelBatchMapRenderer(
            final World worldContext,
            final ResourceLoader loaderToUse,
            final String path,
            final Consumer<Float> callback
    ) {
        this.world = worldContext;
        this.loader = loaderToUse;
        this.modelPath = path;
        this.progressCallback = callback;
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider camera) {
        if (delegate == null) {
            boolean done = loader.update();
            if (progressCallback != null) {
                progressCallback.accept(loader.getProgress());
            }
            if (!done) {
                return;
            }
            Model model = loader.findModel(modelPath);
            ModelInstance instance = model != null ? new ModelInstance(model) : null;
            delegate = new ModelBatchMapRenderer(batch, instance);
            if (progressCallback != null) {
                progressCallback.accept(1f);
            }
        }
        if (map != null) {
            delegate.render(map, camera);
        }
    }

    @Override
    public void dispose() {
        if (delegate != null) {
            delegate.dispose();
        } else {
            batch.dispose();
        }
    }
}
