package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.SpriteBatchMapRenderer;
import com.badlogic.gdx.utils.IntArray;

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
            IntArray updated = dataSystem.consumeUpdatedIndices();
            if (updated.size > 0 && mapRenderer instanceof SpriteBatchMapRenderer sb) {
                sb.invalidateTiles(updated);
            }
            mapData = dataSystem.getRenderData();
        }
        if (mapData == null) {
            return;
        }

        if (mapRenderer != null) {
            mapRenderer.render(mapData, cameraSystem);
        }
    }
}
