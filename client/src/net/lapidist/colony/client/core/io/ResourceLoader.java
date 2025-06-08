package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

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
    private final ObjectMap<String, TextureRegion> textureRegions = new ObjectMap<>();

    /**
     * Load all texture regions from the specified atlas file.
     *
     * @param fileLocationToSet location resolver
     * @param atlasPath path to the .atlas file
     * @throws IOException if the atlas cannot be found
     */
    public void load(final FileLocation fileLocationToSet, final String atlasPath) throws IOException {
        fileLocation = fileLocationToSet;

        if (!fileLocation.getFile(atlasPath).exists()) {
            throw new IOException(String.format("%s does not exist", atlasPath));
        }

        assetManager = new AssetManager(fileLocation.getResolver());
        assetManager.load(atlasPath, TextureAtlas.class);
        assetManager.finishLoading();

        atlas = assetManager.get(atlasPath, TextureAtlas.class);
        textureRegions.clear();
        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            textureRegions.put(region.name, region);
        }

        loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public FileLocation getFileLocation() {
        return fileLocation;
    }

    public ObjectMap<String, TextureRegion> getTextureRegions() {
        return textureRegions;
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            textureRegions.clear();
            if (assetManager != null) {
                assetManager.dispose();
            }
        }
    }

    public boolean isDisposed() {
        return disposed;
    }
}
