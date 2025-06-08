package net.lapidist.colony.client.renderers;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

/**
 * Common interface for entity renderers.
 */
public interface EntityRenderer {
    /**
     * Render the supplied entities.
     *
     * @param entities entities to render
     */
    void render(Array<Entity> entities);
}
