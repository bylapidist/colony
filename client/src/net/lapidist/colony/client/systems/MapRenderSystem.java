package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.components.maps.MapComponent;

public final class MapRenderSystem extends BaseSystem {

    private MapRenderer mapRenderer;

    private MapComponent map;

    private PlayerCameraSystem cameraSystem;

    public MapRenderSystem() {
    }

    public void setMapRenderer(final MapRenderer renderer) {
        this.mapRenderer = renderer;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);

    }

    @Override
    public void dispose() {
        if (mapRenderer instanceof com.badlogic.gdx.utils.Disposable disposable) {
            disposable.dispose();
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

        if (mapRenderer != null) {
            mapRenderer.render(map, cameraSystem);
        }
    }
}
