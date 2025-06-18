package net.lapidist.colony.map;

import net.lapidist.colony.components.state.map.ChunkPos;
import net.lapidist.colony.components.state.map.TilePos;

/**
 * Utility methods for converting world tile coordinates to chunk coordinates
 * and local chunk positions.
 */
public final class MapCoordinateUtils {

    private MapCoordinateUtils() { }

    /**
     * Converts a world tile coordinate to its containing chunk coordinate.
     *
     * @param worldCoord world tile coordinate
     * @return chunk coordinate index
     */
    public static int toChunkCoord(final int worldCoord) {
        return Math.floorDiv(worldCoord, MapChunkData.CHUNK_SIZE);
    }

    /**
     * Converts a world tile coordinate to the local coordinate inside its chunk.
     *
     * @param worldCoord world tile coordinate
     * @return local coordinate within the chunk
     */
    public static int toLocalCoord(final int worldCoord) {
        return Math.floorMod(worldCoord, MapChunkData.CHUNK_SIZE);
    }

    /**
     * Converts world tile coordinates to a {@link ChunkPos} identifying the
     * containing chunk.
     *
     * @param worldX world tile x coordinate
     * @param worldY world tile y coordinate
     * @return chunk position
     */
    public static ChunkPos toChunkPos(final int worldX, final int worldY) {
        return new ChunkPos(toChunkCoord(worldX), toChunkCoord(worldY));
    }

    /**
     * Converts world tile coordinates to a {@link TilePos} representing the
     * local coordinates within the chunk.
     *
     * @param worldX world tile x coordinate
     * @param worldY world tile y coordinate
     * @return local tile position inside the chunk
     */
    public static TilePos toLocalPos(final int worldX, final int worldY) {
        return new TilePos(toLocalCoord(worldX), toLocalCoord(worldY));
    }
}
