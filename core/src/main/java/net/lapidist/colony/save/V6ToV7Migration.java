package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.ChunkPos;
import net.lapidist.colony.map.MapChunkData;

import java.util.Map;

/** Migration from save version 6 to version 7 converting tile maps to chunks. */
public final class V6ToV7Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V6.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V7.number();
    }

    @Override
    public MapState apply(final MapState state) {
        Map<ChunkPos, MapChunkData> existing = state.chunks();
        if (!existing.isEmpty() && existing.keySet().iterator().next() instanceof ChunkPos) {
            return state.toBuilder().version(toVersion()).build();
        }
        Map<ChunkPos, MapChunkData> chunks = new java.util.concurrent.ConcurrentHashMap<>();
        for (Map.Entry<?, ?> e : existing.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (key instanceof TilePos pos && value instanceof TileData tile) {
                int chunkX = Math.floorDiv(pos.x(), MapChunkData.CHUNK_SIZE);
                int chunkY = Math.floorDiv(pos.y(), MapChunkData.CHUNK_SIZE);
                int localX = Math.floorMod(pos.x(), MapChunkData.CHUNK_SIZE);
                int localY = Math.floorMod(pos.y(), MapChunkData.CHUNK_SIZE);
                ChunkPos cp = new ChunkPos(chunkX, chunkY);
                MapChunkData chunk = chunks.computeIfAbsent(cp, p -> new MapChunkData(chunkX, chunkY));
                chunk.getTiles().put(new TilePos(localX, localY), tile);
            }
        }
        return state.toBuilder()
                .chunks(chunks)
                .version(toVersion())
                .build();
    }
}
