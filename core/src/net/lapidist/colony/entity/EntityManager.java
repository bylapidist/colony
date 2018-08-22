package net.lapidist.colony.entity;

import net.lapidist.colony.Constants;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EntityManager {
//    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private TreeMap<String, Entity> entityMap = new TreeMap<String, Entity>();

    /**
     * Return the key of the entity.
     * @param entity Entity.
     * @return Key.
     */
    public String getKey(Entity entity) {
        return Float.toString(entity.getX()) + Float.toString(entity.getY()) + Float.toString(entity.getZ());
    }

    /**
     * Add entity.
     * @param entity The entity to add.
     */
    public void add(Entity entity) {
        String key = getKey(entity);
        entityMap.put(key, entity);
    }

    /**
     * Remove entity.
     * @param entity Entity to remove.
     */
    public void remove(Entity entity) {
        String key = getKey(entity);
        entityMap.remove(key);
    }

    /**
     * Return size of the entity ArrayList.
     * @return Number of elements in entities list.
     */
    public int size() {
        return entityMap.size();
    }

    /**
     * Return entity with specified index.
     * @param key Key.
     * @return Entity at i index.
     */
    public Entity get(String key) {
        return entityMap.get(key);
    }

    /**
     * Return an ArrayList of all entities at the specified tile coordinate.
     * @param x x coord.
     * @param y y coord.
     * @param z z coord.
     * @return List of entities.
     */
    public Entity get(float x, float y, float z) {
        String key = Float.toString(x) + Float.toString(y) + Float.toString(z);

        return entityMap.get(key);
    }

    /**
     * Update all entities.
     */
    public void update() {
        Set<Map.Entry<String, Entity>> entities = entityMap.entrySet();

        for (Map.Entry<String, Entity> entity : entities) {
            entity.getValue().update();
        }
    }

    /**
     * Render all entities.
     * @param delta Delta.
     */
    public void render(float delta) {
        for (int z = 0; z < Constants.MAP_DEPTH; z++) {
            for (int x = Constants.MAP_WIDTH - 1; x >= 0; x--) {
                for (int y = 0; y < Constants.MAP_WIDTH; y++) {
                    Entity entity = this.get(x, y, z);

                    if (entity != null) {
                        entity.render(delta);
                    }
                }
            }
        }
    }

    /**
     * Dispose of all entities.
     */
    public void dispose() {
        entityMap.clear();
    }

    /** Called when window is resized.
     * @param width Width.
     * @param height Height.
     */
    public void resize(int width, int height) {
    }
}
