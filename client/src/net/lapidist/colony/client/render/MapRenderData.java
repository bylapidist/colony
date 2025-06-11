package net.lapidist.colony.client.render;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

/**
 * Read-only data describing map entities for rendering.
 */
public interface MapRenderData {
    Array<Entity> getTiles();
    Array<Entity> getEntities();
}
