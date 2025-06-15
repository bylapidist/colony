package net.lapidist.colony.client.renderers;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.graphics.ShaderManager;
import net.lapidist.colony.client.graphics.ShaderPlugin;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Factory that loads textures and creates sprite batch based renderers for the map system.
 */
public final class SpriteMapRendererFactory implements MapRendererFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteMapRendererFactory.class);

    private final FileLocation fileLocation;
    private final String atlasPath;
    private final ResourceLoader resourceLoader;
    private final java.util.function.Consumer<Float> progressCallback;

    public SpriteMapRendererFactory() {
        this(new TextureAtlasResourceLoader(), FileLocation.INTERNAL, "textures/textures.atlas", null);
    }

    public SpriteMapRendererFactory(
            final ResourceLoader loader,
            final FileLocation location,
            final String path,
            final java.util.function.Consumer<Float> callback
    ) {
        this.fileLocation = location;
        this.atlasPath = path;
        this.resourceLoader = loader;
        this.progressCallback = callback;
    }

    public SpriteMapRendererFactory(final ResourceLoader loader, final FileLocation location, final String path) {
        this(loader, location, path, null);
    }

    public SpriteMapRendererFactory(final FileLocation location, final String path) {
        this(new TextureAtlasResourceLoader(), location, path, null);
    }

    @Override
    public MapRenderer create(final World world, final ShaderPlugin plugin) {
        GraphicsSettings graphics;
        try {
            graphics = Settings.load().getGraphicsSettings();
            resourceLoader.loadTextures(fileLocation, atlasPath, graphics);
        } catch (IOException e) {
            LOGGER.warn("Failed to load textures from {}", atlasPath, e);
            // ignore loading errors in headless tests
            graphics = new GraphicsSettings();
        }
        SpriteBatch batch = new SpriteBatch();

        CameraProvider cameraSystem = world.getSystem(PlayerCameraSystem.class);

        ShaderProgram shader = null;
        if (plugin != null) {
            try {
                shader = plugin.create(new ShaderManager());
            } catch (Exception ex) {
                LOGGER.warn("Shader plugin {} failed", plugin.getClass().getSimpleName(), ex);
            }
        }

        return new LoadingSpriteMapRenderer(
                world,
                batch,
                resourceLoader,
                cameraSystem,
                graphics.isSpriteCacheEnabled(),
                progressCallback,
                shader
        );
    }
}
