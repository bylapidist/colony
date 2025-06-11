package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.settings.GraphicsSettings;

import java.io.IOException;

/**
 * Abstraction for loading graphical assets used by renderers.
 */
public interface ResourceLoader extends Disposable {

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
     * Load a 3D model from the specified path.
     *
     * @param fileLocation location resolver
     * @param modelPath    path to the model file
     * @throws IOException if the model cannot be loaded
     */
    void loadModel(FileLocation fileLocation, String modelPath) throws IOException;

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
     * Retrieve a loaded 3D model by name.
     *
     * @param name model identifier
     * @return matching model or {@code null}
     */
    Model findModel(String name);

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
