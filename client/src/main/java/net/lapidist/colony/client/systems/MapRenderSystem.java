package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.renderers.MapRenderer;

public final class MapRenderSystem extends BaseSystem {

    private MapRenderer mapRenderer;

    private MapRenderData mapData;

    private CameraProvider cameraSystem;

    public MapRenderSystem() {
    }

    public void setMapRenderer(final MapRenderer renderer) {
        this.mapRenderer = renderer;
    }

    public void setCameraProvider(final CameraProvider provider) {
        this.cameraSystem = provider;
    }

    @Override
    public void dispose() {
        if (mapRenderer instanceof com.badlogic.gdx.utils.Disposable disposable) {
            disposable.dispose();
        }
    }

    @Override
    protected void processSystem() {
        MapRenderDataSystem dataSystem = world.getSystem(MapRenderDataSystem.class);
        if (dataSystem != null) {
            MapRenderData newData = dataSystem.getRenderData();
            if (newData != null) {
                mapData = newData;
            }
        }

        if (mapRenderer != null && mapData != null) {
            mapRenderer.render(mapData, cameraSystem);
        }
    }
}
