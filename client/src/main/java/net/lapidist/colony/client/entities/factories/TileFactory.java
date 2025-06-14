package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.ResourceData;
import java.util.Locale;

import static net.lapidist.colony.client.entities.factories.SpriteFactoryUtil.createEntity;

public final class TileFactory {

    private TileFactory() {
    }

    public static Entity create(
            final World world,
            final String tileType,
            final Vector2 coords,
            final boolean passable,
            final boolean selected,
            final ResourceData resources
    ) {
        TileComponent tileComponent = new TileComponent();
        String id = tileType == null ? null : tileType.toLowerCase(Locale.ROOT);
        tileComponent.setTileType(id);
        tileComponent.setPassable(passable);
        tileComponent.setSelected(selected);

        ResourceComponent rc = new ResourceComponent();
        if (resources != null) {
            rc.setWood(resources.wood());
            rc.setStone(resources.stone());
            rc.setFood(resources.food());
        }

        return createEntity(world, tileComponent, rc, coords);
    }
}
