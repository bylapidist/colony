package net.lapidist.colony.client.renderers;

import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.G3dResourceLoader;
import net.lapidist.colony.client.core.io.ResourceLoader;

/**
 * Factory creating a simple {@link ModelBatchMapRenderer} using {@link G3dResourceLoader}.
 */
public final class ModelBatchMapRendererFactory implements MapRendererFactory {

    private final FileLocation location;
    private final String modelPath;
    private final ResourceLoader loader;

    public ModelBatchMapRendererFactory() {
        this(new G3dResourceLoader(), FileLocation.INTERNAL, "models/cube.g3dj");
    }

    public ModelBatchMapRendererFactory(
            final ResourceLoader loaderToSet,
            final FileLocation locationToSet,
            final String path
    ) {
        this.location = locationToSet;
        this.modelPath = path;
        this.loader = loaderToSet;
    }

    @Override
    public MapRenderer create(final World world) {
        try {
            loader.loadTextures(location, "textures/textures.atlas");
            loader.loadModel(location, modelPath);
        } catch (Exception e) {
            // ignore errors in headless tests
        }
        Model model = loader.findModel(modelPath);
        ModelInstance instance = model != null ? new ModelInstance(model) : null;
        return new ModelBatchMapRenderer(new ModelBatch(), instance);
    }
}
