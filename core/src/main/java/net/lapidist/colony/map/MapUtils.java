package net.lapidist.colony.map;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import com.artemis.ComponentMapper;
import java.util.Optional;

/**
 * Utility methods for locating map entities within an Artemis {@link World}.
 */
public final class MapUtils {

    private MapUtils() { }

    /**
     * Returns the first entity in the world containing a {@link MapComponent}.
     *
     * @param world the Artemis world to search
     * @return an {@link Optional} containing the map entity if present
     */
    public static Optional<Entity> findMapEntity(final World world) {
        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        if (maps.size() > 0) {
            return Optional.of(world.getEntity(maps.get(0)));
        }
        return Optional.empty();
    }

    /**
     * Returns the {@link MapComponent} from the first map entity in the world.
     *
     * @param world the Artemis world to search
     * @return an {@link Optional} containing the map component if present
     */
    public static Optional<MapComponent> findMap(final World world) {
        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        if (maps.size() > 0) {
            return Optional.of(
                    world.getMapper(MapComponent.class).get(maps.get(0))
            );
        }
        return Optional.empty();
    }

    /**
     * Finds a tile entity at the given map coordinates.
     *
     * @param map    the map component containing tiles
     * @param x      the tile x coordinate
     * @param y      the tile y coordinate
     * @param mapper mapper for {@link TileComponent} instances
     * @return an {@link Optional} containing the tile entity if present
     */
    public static Optional<Entity> findTile(
            final MapComponent map,
            final int x,
            final int y,
            final ComponentMapper<TileComponent> mapper
    ) {
        if (map == null) {
            return Optional.empty();
        }

        Entity tile = map.getTileMap().get(new net.lapidist.colony.components.state.TilePos(x, y));
        if (tile != null) {
            return Optional.of(tile);
        }

        return Optional.empty();
    }
}
