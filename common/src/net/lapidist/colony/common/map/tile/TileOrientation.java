package net.lapidist.colony.common.map.tile;

import com.badlogic.gdx.math.Vector2;

public enum TileOrientation {

    SOUTH(new Vector2(0, -1)), SOUTHEAST(null),
    EAST(new Vector2(1, 0)), NORTHEAST(null),
    NORTH(new Vector2(0, 1)), NORTHWEST(null),
    WEST(new Vector2(-1, 0)), SOUTHWEST(null);

    private Vector2 vector;

    TileOrientation(Vector2 vector) {
        this.vector = vector;
    }

    public Vector2 getVector() {
        return vector;
    }

    @Override
    public String toString() {
        switch (this) {
            case SOUTH: return "South";
            case SOUTHEAST: return "Southeast";
            case EAST: return "East";
            case NORTHEAST: return "Northeast";
            case NORTH: return "North";
            case NORTHWEST: return "Northwest";
            case WEST: return "West";
            case SOUTHWEST: return "Southwest";
            default: throw new IllegalArgumentException();
        }
    }
}
