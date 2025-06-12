package net.lapidist.colony.client.renderers;

import com.artemis.World;
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
    private final java.util.function.Consumer<Float> progressCallback;

    public ModelBatchMapRendererFactory() {
        this(new G3dResourceLoader(), FileLocation.INTERNAL, "models/cube.g3dj", null);
    }

    public ModelBatchMapRendererFactory(
            final ResourceLoader loaderToSet,
            final FileLocation locationToSet,
            final String path,
            final java.util.function.Consumer<Float> callback
    ) {
        this.location = locationToSet;
        this.modelPath = path;
        this.loader = loaderToSet;
        this.progressCallback = callback;
    }

    public ModelBatchMapRendererFactory(
            final ResourceLoader loaderToSet,
            final FileLocation locationToSet,
            final String path
    ) {
        this(loaderToSet, locationToSet, path, null);
    }

    @Override
    public MapRenderer create(final World world) {
        try {
            loader.loadTextures(location, "textures/textures.atlas");
            loader.loadModel(location, modelPath);
        } catch (Exception e) {
            // ignore errors in headless tests
        }

        return new LoadingModelBatchMapRenderer(
                world,
                loader,
                modelPath,
                progressCallback
        );
    }
}
