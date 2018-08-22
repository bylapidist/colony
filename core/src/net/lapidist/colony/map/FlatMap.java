package net.lapidist.colony.map;

import net.lapidist.colony.entity.tiles.GrassTile;

import java.util.Random;

public class FlatMap extends Map {

    /**
     * Constructor for FlatMap.
     * @param width Width of map.
     * @param height Height of map.
     */
    public FlatMap(int width, int height, int depth) {
        super(width, height, depth);
        generateMap();
    }

    private void generateMap() {
        for (float x = 0; x < getWidth(); x++) {
            for (float y = 0; y < getHeight(); y++) {
                game.entityManager.add(new GrassTile(x, y, 0, (byte) new Random().nextInt(6)));
            }
        }
    }
}
