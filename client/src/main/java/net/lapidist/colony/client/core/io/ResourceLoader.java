package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.settings.GraphicsSettings;

import java.io.IOException;

/**
 * Abstraction for loading graphical assets used by renderers.
 */
public interface ResourceLoader extends Disposable {

    /** Default exponent for specular highlights. */
    float DEFAULT_SPECULAR_POWER = 16f;

    /**
     * Load texture regions from the given atlas.
     *
     * @param fileLocation location resolver
     * @param atlasPath    path to the texture atlas
     * @param graphicsSettings optional texture settings
     * @throws IOException if the atlas cannot be loaded
     */
    void loadTextures(
            FileLocation fileLocation,
            String atlasPath,
            GraphicsSettings graphicsSettings
    ) throws IOException;

    /**
     * Convenience overload without graphics settings.
     *
     * @param fileLocation location resolver
     * @param atlasPath    path to the texture atlas
     * @throws IOException if the atlas cannot be loaded
     */
    void loadTextures(FileLocation fileLocation, String atlasPath) throws IOException;


    /**
     * Check whether assets have been loaded.
     *
     * @return {@code true} if the loader has loaded resources
     */
    boolean isLoaded();

    /**
     * Access the atlas used for texture lookups.
     *
     * @return loaded texture atlas or {@code null}
     */
    TextureAtlas getAtlas();

    /**
     * Find a texture region by name.
     *
     * @param name region identifier
     * @return matching region or {@code null}
     */
    TextureRegion findRegion(String name);

    /**
     * Retrieve the normal map associated with a region if present.
     *
     * @param name base region identifier
     * @return matching normal map region or {@code null}
     */
    default TextureRegion findNormalRegion(final String name) {
        return null;
    }

    /**
     * Retrieve the specular map associated with a region if present.
     *
     * @param name base region identifier
     * @return matching specular map region or {@code null}
     */
    default TextureRegion findSpecularRegion(final String name) {
        return null;
    }

    /**
     * Retrieve the specular power associated with a region if present.
     *
     * @param name base region identifier
     * @return exponent controlling specular highlights
     */
    default float getSpecularPower(final String name) {
        return DEFAULT_SPECULAR_POWER;
    }

    /**
     * Load a particle effect from the given path. Implementations should look
     * for definitions under an {@code effects/} directory.
     *
     * @param effectPath effect definition to load
     * @return parsed effect
     * @throws IOException if the file cannot be found or read
     */
    ParticleEffect loadEffect(String effectPath) throws IOException;

    /** Returns the file location used to resolve assets. */
    FileLocation getFileLocation();


    /**
     * Progress the asynchronous loading process once.
     *
     * @return {@code true} when all assets are loaded
     */
    boolean update();

    /**
     * Current loading progress between 0 and 1.
     *
     * @return progress value
     */
    float getProgress();

    /**
     * Block until all queued assets have finished loading.
     */
    default void finishLoading() {
        while (!update()) {
            Thread.yield();
        }
    }
}
