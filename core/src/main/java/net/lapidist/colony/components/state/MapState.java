package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;
import net.lapidist.colony.save.SaveVersion;

import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@KryoType
public record MapState(
        int version,
        String name,
        String saveName,
        String autosaveName,
        String description,
        Map<ChunkPos, MapChunkData> chunks,
        List<BuildingData> buildings,
        ResourceData playerResources
) {
    public static final int CURRENT_VERSION = SaveVersion.CURRENT.number();

    public MapState() {
        this(
                CURRENT_VERSION,
                "map-" + UUID.randomUUID(),
                "save-" + UUID.randomUUID(),
                null,
                null,
                new HashMap<>(),
                new ArrayList<>(),
                new ResourceData()
        );
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public MapChunkData getOrCreateChunk(final int chunkX, final int chunkY) {
        ChunkPos pos = new ChunkPos(chunkX, chunkY);
        return chunks.computeIfAbsent(pos, p -> new MapChunkData(chunkX, chunkY));
    }

    public TileData getTile(final int x, final int y) {
        int chunkX = Math.floorDiv(x, MapChunkData.CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, MapChunkData.CHUNK_SIZE);
        int localX = Math.floorMod(x, MapChunkData.CHUNK_SIZE);
        int localY = Math.floorMod(y, MapChunkData.CHUNK_SIZE);
        return getOrCreateChunk(chunkX, chunkY).getTile(localX, localY);
    }

    public void putTile(final TileData tile) {
        int chunkX = Math.floorDiv(tile.x(), MapChunkData.CHUNK_SIZE);
        int chunkY = Math.floorDiv(tile.y(), MapChunkData.CHUNK_SIZE);
        int localX = Math.floorMod(tile.x(), MapChunkData.CHUNK_SIZE);
        int localY = Math.floorMod(tile.y(), MapChunkData.CHUNK_SIZE);
        getOrCreateChunk(chunkX, chunkY).getTiles().put(new TilePos(localX, localY), tile);
    }

    public Map<TilePos, TileData> tiles() {
        return new TileMapView();
    }

    private class TileMapView extends java.util.AbstractMap<TilePos, TileData> {
        @Override
        public TileData get(final Object key) {
            if (key instanceof TilePos pos) {
                return MapState.this.getTile(pos.x(), pos.y());
            }
            return null;
        }

        @Override
        public TileData put(final TilePos key, final TileData value) {
            int chunkX = Math.floorDiv(key.x(), MapChunkData.CHUNK_SIZE);
            int chunkY = Math.floorDiv(key.y(), MapChunkData.CHUNK_SIZE);
            int localX = Math.floorMod(key.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(key.y(), MapChunkData.CHUNK_SIZE);
            return MapState.this.getOrCreateChunk(chunkX, chunkY)
                    .getTiles()
                    .put(new TilePos(localX, localY), value);
        }

        @Override
        public java.util.Set<Entry<TilePos, TileData>> entrySet() {
            java.util.Map<TilePos, TileData> all = new java.util.HashMap<>();
            for (var chunkEntry : chunks.entrySet()) {
                for (var entry : chunkEntry.getValue().getTiles().entrySet()) {
                    TileData td = entry.getValue();
                    all.put(new TilePos(td.x(), td.y()), td);
                }
            }
            return all.entrySet();
        }

        @Override
        public int size() {
            return chunks.values().stream().mapToInt(c -> c.getTiles().size()).sum();
        }
    }

    public static final class Builder {
        private int version;
        private String name;
        private String saveName;
        private String autosaveName;
        private String description;
        private Map<ChunkPos, MapChunkData> chunks;
        private List<BuildingData> buildings;
        private ResourceData playerResources;

        private Builder() {
            this(new MapState());
        }

        private Builder(final MapState state) {
            this.version = state.version;
            this.name = state.name;
            this.saveName = state.saveName;
            this.autosaveName = state.autosaveName;
            this.description = state.description;
            this.chunks = state.chunks;
            this.buildings = state.buildings;
            this.playerResources = state.playerResources;
        }

        public Builder version(final int newVersion) {
            this.version = newVersion;
            return this;
        }

        public Builder name(final String newName) {
            this.name = newName;
            return this;
        }

        public Builder saveName(final String newSaveName) {
            this.saveName = newSaveName;
            return this;
        }

        public Builder autosaveName(final String newAutosaveName) {
            this.autosaveName = newAutosaveName;
            return this;
        }

        public Builder description(final String newDescription) {
            this.description = newDescription;
            return this;
        }

        public Builder chunks(final Map<ChunkPos, MapChunkData> newChunks) {
            this.chunks = newChunks;
            return this;
        }

        public Builder buildings(final List<BuildingData> newBuildings) {
            this.buildings = newBuildings;
            return this;
        }

        public Builder playerResources(final ResourceData newResources) {
            this.playerResources = newResources;
            return this;
        }

        public MapState build() {
            return new MapState(version, name, saveName, autosaveName, description, chunks, buildings, playerResources);
        }
    }
}
