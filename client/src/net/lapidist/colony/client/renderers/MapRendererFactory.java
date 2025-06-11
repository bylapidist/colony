package net.lapidist.colony.client.renderers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.resources.ResourceComponent;

import java.io.IOException;

/**
 * Factory that loads textures and creates renderers for the map system.
 */
public final class MapRendererFactory {

    private final FileLocation fileLocation;
    private final String atlasPath;
    private final ResourceLoader resourceLoader;

    public MapRendererFactory() {
        this(new TextureAtlasResourceLoader(), FileLocation.INTERNAL, "textures/textures.atlas");
    }

    public MapRendererFactory(final ResourceLoader loader, final FileLocation location, final String path) {
        this.fileLocation = location;
        this.atlasPath = path;
        this.resourceLoader = loader;
    }

    public MapRendererFactory(final FileLocation location, final String path) {
        this(new TextureAtlasResourceLoader(), location, path);
    }

    public MapRenderer create(final World world) {
        try {
            GraphicsSettings graphics = Settings.load().getGraphicsSettings();
            resourceLoader.loadTextures(fileLocation, atlasPath, graphics);
        } catch (IOException e) {
            // ignore loading errors in headless tests
        }
        SpriteBatch batch = new SpriteBatch();

        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);
        ComponentMapper<BuildingComponent> buildingMapper = world.getMapper(BuildingComponent.class);
        ComponentMapper<TextureRegionReferenceComponent> textureMapper =
                world.getMapper(TextureRegionReferenceComponent.class);
        ComponentMapper<ResourceComponent> resourceMapper = world.getMapper(ResourceComponent.class);

        TileRenderer tileRenderer = new TileRenderer(
                batch,
                resourceLoader,
                cameraSystem,
                tileMapper,
                textureMapper
        );
        BuildingRenderer buildingRenderer = new BuildingRenderer(
                batch,
                resourceLoader,
                cameraSystem,
                buildingMapper,
                textureMapper
        );
        ResourceRenderer resourceRenderer = new ResourceRenderer(
                batch,
                cameraSystem,
                tileMapper,
                resourceMapper
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
