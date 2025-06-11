package net.lapidist.colony.client.renderers;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.components.maps.MapComponent;

import java.io.IOException;

/**
 * Factory that loads textures and creates sprite batch based renderers for the map system.
 */
public final class SpriteMapRendererFactory implements MapRendererFactory {

    private final FileLocation fileLocation;
    private final String atlasPath;
    private final ResourceLoader resourceLoader;

    public SpriteMapRendererFactory() {
        this(new TextureAtlasResourceLoader(), FileLocation.INTERNAL, "textures/textures.atlas");
    }

    public SpriteMapRendererFactory(final ResourceLoader loader, final FileLocation location, final String path) {
        this.fileLocation = location;
        this.atlasPath = path;
        this.resourceLoader = loader;
    }

    public SpriteMapRendererFactory(final FileLocation location, final String path) {
        this(new TextureAtlasResourceLoader(), location, path);
    }

    @Override
    public MapRenderer create(final World world) {
        try {
            GraphicsSettings graphics = Settings.load().getGraphicsSettings();
            resourceLoader.loadTextures(fileLocation, atlasPath, graphics);
        } catch (IOException e) {
            // ignore loading errors in headless tests
        }
        SpriteBatch batch = new SpriteBatch();

        CameraProvider cameraSystem = world.getSystem(PlayerCameraSystem.class);

        TileRenderer tileRenderer = new TileRenderer(
                batch,
                resourceLoader,
                cameraSystem,
                new DefaultAssetResolver()
        );
        BuildingRenderer buildingRenderer = new BuildingRenderer(
                batch,
                resourceLoader,
                cameraSystem,
                new DefaultAssetResolver()
        );
        ResourceRenderer resourceRenderer = new ResourceRenderer(
                batch,
                cameraSystem
        );

        // trigger map mapper initialization so MapRenderSystem can use it immediately
        world.getMapper(MapComponent.class);

        return new SpriteBatchMapRenderer(
                batch,
                resourceLoader,
                tileRenderer,
                buildingRenderer,
                resourceRenderer
        );
    }
}
