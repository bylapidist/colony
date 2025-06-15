package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
        TextureAtlasLoader.TextureAtlasParameter parameter = null;
        if (graphicsSettings != null && graphicsSettings.isMipMapsEnabled()) {
            parameter = new TextureAtlasLoader.TextureAtlasParameter();
            // Recent LibGDX versions expose a genMipMaps field which we would
            // normally set here. The 1.13.x line bundled with the project does
            // not provide such a field, so mipmaps are generated manually once
            // loading completes.
        }
        assetManager.load(atlasPath, TextureAtlas.class, parameter);
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

    @Override
    public ParticleEffect loadEffect(final String effectPath) throws IOException {
        if (fileLocation == null) {
            throw new IOException("file location not set");
        }
        var file = fileLocation.getFile(effectPath);
        if (!file.exists()) {
            throw new IOException(String.format("%s does not exist", effectPath));
        }
        ParticleEffect effect = new ParticleEffect();
        effect.load(file, file.parent());
        return effect;
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
                com.badlogic.gdx.utils.ObjectSet<Texture> updated = new com.badlogic.gdx.utils.ObjectSet<>();
                java.util.Map<Texture, Texture> remapped = new java.util.HashMap<>();
                for (Texture texture : atlas.getTextures()) {
                    Texture result = texture;
                    if (pendingSettings.isMipMapsEnabled()
                            && !texture.getTextureData().useMipMaps()) {
                        if (texture.getTextureData()
                                instanceof com.badlogic.gdx.graphics.glutils.FileTextureData data) {
                            result = new Texture(data.getFileHandle(), data.getFormat(), true);
                            result.setWrap(texture.getUWrap(), texture.getVWrap());
                            texture.dispose();
                            remapped.put(texture, result);
                        }
                    }
                    result.setFilter(minFilter, magFilter);
                    if (pendingSettings.isAnisotropicFilteringEnabled()) {
                        result.setAnisotropicFilter(GLTexture.getMaxAnisotropicFilterLevel());
                    }
                    updated.add(result);
                }
                if (!remapped.isEmpty()) {
                    for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                        Texture replace = remapped.get(region.getTexture());
                        if (replace != null) {
                            region.setTexture(replace);
                        }
                    }
                    atlas.getTextures().clear();
                    atlas.getTextures().addAll(updated);
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
    public TextureRegion findNormalRegion(final String name) {
        if (atlas == null) {
            return null;
        }
        return atlas.findRegion(name + "_n");
    }

    @Override
    public TextureRegion findSpecularRegion(final String name) {
        if (atlas == null) {
            return null;
        }
        return atlas.findRegion(name + "_s");
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
