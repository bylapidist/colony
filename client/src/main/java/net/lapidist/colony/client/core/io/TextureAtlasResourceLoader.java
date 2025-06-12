package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.settings.GraphicsSettings;

import java.io.IOException;

/**
 * {@link ResourceLoader} implementation that loads textures from a
 * {@link TextureAtlas}.
 */
public final class TextureAtlasResourceLoader implements ResourceLoader {

    private boolean disposed;
    private boolean loaded;

    private FileLocation fileLocation;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private String pendingAtlasPath;
    private GraphicsSettings pendingSettings;

    /**
     * Load all texture regions from the specified atlas file.
     *
     * @param fileLocationToSet location resolver
     * @param atlasPath path to the .atlas file
     * @throws IOException if the atlas cannot be found
     */
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
        pendingSettings = graphicsSettings;
        loaded = false;
    }

    public void loadTextures(final FileLocation fileLocationToSet, final String atlasPath) throws IOException {
        loadTextures(fileLocationToSet, atlasPath, null);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public FileLocation getFileLocation() {
        return fileLocation;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public boolean update() {
        if (assetManager == null) {
            return false;
        }
        boolean done = assetManager.update();
        if (done && !loaded) {
            atlas = assetManager.get(pendingAtlasPath, TextureAtlas.class);
            if (pendingSettings != null) {
                Texture.TextureFilter minFilter = pendingSettings.isMipMapsEnabled()
                        ? Texture.TextureFilter.MipMapLinearLinear
                        : Texture.TextureFilter.Linear;
                Texture.TextureFilter magFilter = Texture.TextureFilter.Linear;
                for (Texture texture : atlas.getTextures()) {
                    texture.setFilter(minFilter, magFilter);
                    if (pendingSettings.isAnisotropicFilteringEnabled()) {
                        texture.setAnisotropicFilter(GLTexture.getMaxAnisotropicFilterLevel());
                    }
                }
            }
            loaded = true;
        }
        return done;
    }

    @Override
    public float getProgress() {
        return assetManager == null ? 0f : assetManager.getProgress();
    }


    /**
     * Find a texture region within the loaded atlas by name.
     *
     * @param name region name
     * @return the {@link TextureRegion} or {@code null} if not found
     */
    public TextureRegion findRegion(final String name) {
        if (atlas == null) {
            return null;
        }
        return atlas.findRegion(name);
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

    public boolean isDisposed() {
        return disposed;
    }
}
