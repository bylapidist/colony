package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.settings.GraphicsSettings;
import com.badlogic.gdx.utils.Disposable;

import java.io.IOException;

/**
 * Simplified resource loader using LibGDX's {@link TextureAtlas} to
 * manage textures. This replaces the previous implementation which
 * parsed a custom configuration file.
 */
public final class ResourceLoader implements Disposable {

    private boolean disposed;
    private boolean loaded;

    private FileLocation fileLocation;
    private AssetManager assetManager;
    private TextureAtlas atlas;

    /**
     * Load all texture regions from the specified atlas file.
     *
     * @param fileLocationToSet location resolver
     * @param atlasPath path to the .atlas file
     * @throws IOException if the atlas cannot be found
     */
    public void load(
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

        if (graphicsSettings != null) {
            Texture.TextureFilter minFilter = graphicsSettings.isMipMapsEnabled()
                    ? Texture.TextureFilter.MipMapLinearLinear
                    : Texture.TextureFilter.Linear;
            Texture.TextureFilter magFilter = Texture.TextureFilter.Linear;
            for (Texture texture : atlas.getTextures()) {
                texture.setFilter(minFilter, magFilter);
                if (graphicsSettings.isAnisotropicFilteringEnabled()) {
                    texture.setAnisotropicFilter(GLTexture.getMaxAnisotropicFilterLevel());
                }
            }
        }

        loaded = true;
    }

    public void load(final FileLocation fileLocationToSet, final String atlasPath) throws IOException {
        load(fileLocationToSet, atlasPath, null);
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
