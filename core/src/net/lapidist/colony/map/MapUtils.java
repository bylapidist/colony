package net.lapidist.colony.map;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import net.lapidist.colony.components.maps.MapComponent;
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
        return findMapEntity(world)
                .map(e -> world.getMapper(MapComponent.class).get(e));
    }
}
