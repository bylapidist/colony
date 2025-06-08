package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.MapRenderers;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.components.maps.MapComponent;

public final class MapRenderSystem extends BaseSystem {

    private final MapRendererFactory factory;
    private MapRenderers renderers;

    private Entity map;

    private PlayerCameraSystem cameraSystem;

    private ComponentMapper<MapComponent> mapMapper;

    private TileRenderer tileRenderer;
    private BuildingRenderer buildingRenderer;

    public MapRenderSystem(final MapRendererFactory factoryToUse) {
        this.factory = factoryToUse;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        mapMapper = world.getMapper(MapComponent.class);

        renderers = factory.create(world);
        tileRenderer = renderers.getTileRenderer();
        buildingRenderer = renderers.getBuildingRenderer();

        map = net.lapidist.colony.map.MapUtils.findMapEntity(world).orElse(null);
    }

    @Override
    public void dispose() {
        if (renderers != null) {
            renderers.dispose();
        }
    }

    @Override
    protected void processSystem() {
        SpriteBatch batch = renderers.getSpriteBatch();
        batch.setProjectionMatrix(cameraSystem.getCamera().combined);
        batch.begin();

        MapComponent mapComponent = mapMapper.get(map);
        tileRenderer.render(mapComponent.getTiles());
        buildingRenderer.render(mapComponent.getEntities());

        batch.end();
    }
}
