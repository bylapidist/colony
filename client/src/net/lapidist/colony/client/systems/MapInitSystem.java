package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.MapFactory;
import net.lapidist.colony.client.entities.factories.RenderComponentFactory;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.components.maps.MapComponent;

/**
 * Initializes the game world using a {@link MapStateProvider}.
 */
public class MapInitSystem extends BaseSystem {
    private final MapStateProvider provider;

    public MapInitSystem(final MapStateProvider providerToSet) {
        this.provider = providerToSet;
    }

    @Override
    public final void initialize() {
        var mapEntity = MapFactory.create(world, provider.provide());
        var map = mapEntity.getComponent(MapComponent.class);
        var resolver = new DefaultAssetResolver();
        for (int i = 0; i < map.getTiles().size; i++) {
            RenderComponentFactory.addTileRendering(world, map.getTiles().get(i), resolver);
        }
        for (int i = 0; i < map.getEntities().size; i++) {
            RenderComponentFactory.addBuildingRendering(world, map.getEntities().get(i), resolver);
        }
    }

    @Override
    protected final void processSystem() {
        // initialization occurs once in initialize
    }
}
