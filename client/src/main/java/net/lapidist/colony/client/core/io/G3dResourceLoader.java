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
    private String pendingAtlasPath;
    private String pendingModelPath;

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
        pendingAtlasPath = atlasPath;
        loaded = false;
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
        pendingModelPath = modelPath;
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
    public boolean update() {
        if (assetManager == null) {
            return false;
        }
        boolean done = assetManager.update();
        if (done && !loaded) {
            if (pendingAtlasPath != null) {
                atlas = assetManager.get(pendingAtlasPath, TextureAtlas.class);
            }
            loaded = true;
        }
        return done;
    }

    @Override
    public float getProgress() {
        return assetManager == null ? 0f : assetManager.getProgress();
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
