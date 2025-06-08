package net.lapidist.colony.map;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import java.util.Optional;
import net.lapidist.colony.components.maps.MapComponent;

/**
 * Utility methods for locating map entities within an Artemis {@link World}.
 */
public final class MapUtils {

    private MapUtils() { }

    /**
     * Returns the first entity in the world containing a {@link MapComponent}.
     *
     * @param world the Artemis world to search
     * @return the map entity or {@code null} if none exists
     */
    public static Entity findMapEntity(final World world) {
        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        if (maps.size() > 0) {
            return world.getEntity(maps.get(0));
        }
        return null;
    }

    /**
     * Returns the {@link MapComponent} from the first map entity in the world.
     *
     * @param world the Artemis world to search
     * @return an {@link Optional} containing the map component or empty if none exists
     */
    public static Optional<MapComponent> findMap(final World world) {
        Entity entity = findMapEntity(world);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(world.getMapper(MapComponent.class).get(entity));
    }
}
