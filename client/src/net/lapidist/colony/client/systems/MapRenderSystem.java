package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.TileComponent;

public class MapRenderSystem extends EntitySystem {

    private ImmutableArray<Entity> tiles;

    public MapRenderSystem() {
    }

    @Override
    public final void addedToEngine(final Engine engine) {
        tiles = engine.getEntitiesFor(
                Family.all(TileComponent.class).get()
        );
    }

    @Override
    public final void update(final float deltaTime) {
        for (int i = 0; i < tiles.size(); ++i) {
            Entity entity = tiles.get(i);
//            TileComponent tile = entity.getComponent(TileComponent.class);
            TextureRegionReferenceComponent textureRegionReferenceComponent =
                    entity.getComponent(TextureRegionReferenceComponent.class);

            System.out.println(textureRegionReferenceComponent.getResourceRef());
        }
    }
}
