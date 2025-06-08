package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import java.io.IOException;

public final class MapRenderSystem extends BaseSystem {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private Entity map;

    private PlayerCameraSystem cameraSystem;

    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<BuildingComponent> buildingMapper;
    private ComponentMapper<TextureRegionReferenceComponent> textureMapper;

    private TileRenderer tileRenderer;
    private BuildingRenderer buildingRenderer;

    public MapRenderSystem() {
    }

    @Override
    public void initialize() {
        try {
            resourceLoader.load(FileLocation.INTERNAL, "textures/textures.atlas");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        mapMapper = world.getMapper(MapComponent.class);
        tileMapper = world.getMapper(TileComponent.class);
        buildingMapper = world.getMapper(BuildingComponent.class);
        textureMapper = world.getMapper(TextureRegionReferenceComponent.class);

        tileRenderer = new TileRenderer(
                spriteBatch,
                resourceLoader,
                cameraSystem,
                tileMapper,
                textureMapper
        );

        buildingRenderer = new BuildingRenderer(
                spriteBatch,
                resourceLoader,
                cameraSystem,
                buildingMapper,
                textureMapper
        );

        map = net.lapidist.colony.map.MapUtils.findMapEntity(world);
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        spriteBatch.dispose();
    }

    @Override
    protected void processSystem() {
        spriteBatch.setProjectionMatrix(cameraSystem.getCamera().combined);
        spriteBatch.begin();

        MapComponent mapComponent = mapMapper.get(map);
        tileRenderer.render(mapComponent.getTiles());
        buildingRenderer.render(mapComponent.getEntities());

        spriteBatch.end();
    }
}
