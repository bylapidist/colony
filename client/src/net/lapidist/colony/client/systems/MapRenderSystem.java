package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.MapRendererFactory;
import net.lapidist.colony.client.renderers.MapRenderers;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.client.renderers.ResourceRenderer;
import net.lapidist.colony.components.maps.MapComponent;

public final class MapRenderSystem extends BaseSystem {

    private final MapRendererFactory factory;
    private MapRenderers renderers;

    private MapComponent map;

    private PlayerCameraSystem cameraSystem;


    private TileRenderer tileRenderer;
    private BuildingRenderer buildingRenderer;
    private ResourceRenderer resourceRenderer;

    public MapRenderSystem(final MapRendererFactory factoryToUse) {
        this.factory = factoryToUse;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        renderers = factory.create(world);
        tileRenderer = renderers.getTileRenderer();
        buildingRenderer = renderers.getBuildingRenderer();
        resourceRenderer = renderers.getResourceRenderer();

    }

    @Override
    public void dispose() {
        if (renderers != null) {
            renderers.dispose();
        }
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = net.lapidist.colony.map.MapUtils.findMap(world).orElse(null);
            if (map == null) {
                return;
            }
        }

        SpriteBatch batch = renderers.getSpriteBatch();
        batch.setProjectionMatrix(cameraSystem.getCamera().combined);
        batch.begin();

        tileRenderer.render(map.getTiles());
        buildingRenderer.render(map.getEntities());
        resourceRenderer.render(map.getTiles());

        batch.end();
    }
}
