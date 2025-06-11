package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import net.lapidist.colony.settings.GraphicsSettings;

import java.io.IOException;

/**
 * Resource loader capable of handling both texture atlases and g3d models.
 */
public final class G3dResourceLoader implements ResourceLoader {

    private boolean disposed;
    private boolean loaded;

    private FileLocation fileLocation;
    private AssetManager assetManager;
    private TextureAtlas atlas;

    @Override
    public void loadTextures(
            final FileLocation fileLocationToSet,
            final String atlasPath,
            final GraphicsSettings graphicsSettings
    ) throws IOException {
        fileLocation = fileLocationToSet;

        if (!fileLocation.getFile(atlasPath).exists()) {
            throw new IOException(String.format("%s does not exist", atlasPath));
        }

        assetManager = new AssetManager(fileLocation.getResolver());
        assetManager.load(atlasPath, TextureAtlas.class);
        assetManager.finishLoading();

        atlas = assetManager.get(atlasPath, TextureAtlas.class);

        loaded = true;
    }

    @Override
    public void loadTextures(final FileLocation fileLocationToSet, final String atlasPath) throws IOException {
        loadTextures(fileLocationToSet, atlasPath, null);
    }

    @Override
    public void loadModel(final FileLocation fileLocationToSet, final String modelPath) throws IOException {
        fileLocation = fileLocationToSet;

        if (!fileLocation.getFile(modelPath).exists()) {
            throw new IOException(String.format("%s does not exist", modelPath));
        }

        if (assetManager == null) {
            assetManager = new AssetManager(fileLocation.getResolver());
        }
        assetManager.load(modelPath, Model.class);
        assetManager.finishLoading();

        loaded = true;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public TextureRegion findRegion(final String name) {
        if (atlas == null) {
            return null;
        }
        return atlas.findRegion(name);
    }

    @Override
    public Model findModel(final String name) {
        if (assetManager == null || !assetManager.isLoaded(name)) {
            return null;
        }
        return assetManager.get(name, Model.class);
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            if (assetManager != null) {
                assetManager.dispose();
            }
        }
    }
}
