package net.lapidist.colony.components.state.map;

import net.lapidist.colony.serialization.KryoType;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.resources.ResourceData;

import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.map.MapCoordinateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
        ResourceData playerResources,
        Map<String, Integer> inventory,
        PlayerPosition playerPos,
        CameraPosition cameraPos,
        EnvironmentState environment,
        int width,
        int height
) {
    public static final int CURRENT_VERSION = SaveVersion.CURRENT.number();
    public static final int DEFAULT_WIDTH = 30;
    public static final int DEFAULT_HEIGHT = 30;

    public MapState() {
        this(
                CURRENT_VERSION,
                "map-" + UUID.randomUUID(),
                "save-" + UUID.randomUUID(),
                null,
                null,
                new ConcurrentHashMap<>(),
                new ArrayList<>(),
                new ResourceData(),
                new java.util.HashMap<>(),
                new PlayerPosition(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2),
                new CameraPosition(DEFAULT_WIDTH / 2f, DEFAULT_HEIGHT / 2f),
                new EnvironmentState(),
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
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
        int chunkX = MapCoordinateUtils.toChunkCoord(x);
        int chunkY = MapCoordinateUtils.toChunkCoord(y);
        int localX = MapCoordinateUtils.toLocalCoord(x);
        int localY = MapCoordinateUtils.toLocalCoord(y);
        return getOrCreateChunk(chunkX, chunkY).getTile(localX, localY);
    }

    public void putTile(final TileData tile) {
        int chunkX = MapCoordinateUtils.toChunkCoord(tile.x());
        int chunkY = MapCoordinateUtils.toChunkCoord(tile.y());
        int localX = MapCoordinateUtils.toLocalCoord(tile.x());
        int localY = MapCoordinateUtils.toLocalCoord(tile.y());
        getOrCreateChunk(chunkX, chunkY).getTiles().put(new TilePos(localX, localY), tile);
    }

    public Map<TilePos, TileData> tiles() {
        return new TileMapView();
    }

    private final class TileMapView extends java.util.AbstractMap<TilePos, TileData> {
        @Override
        public TileData get(final Object key) {
            if (key instanceof TilePos pos) {
                return MapState.this.getTile(pos.x(), pos.y());
            }
            return null;
        }

        @Override
        public TileData put(final TilePos key, final TileData value) {
            int chunkX = MapCoordinateUtils.toChunkCoord(key.x());
            int chunkY = MapCoordinateUtils.toChunkCoord(key.y());
            int localX = MapCoordinateUtils.toLocalCoord(key.x());
            int localY = MapCoordinateUtils.toLocalCoord(key.y());
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
        private Map<String, Integer> inventory;
        private PlayerPosition playerPos;
        private CameraPosition cameraPos;
        private EnvironmentState environment;
        private int width;
        private int height;

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
            this.inventory = state.inventory;
            this.playerPos = state.playerPos;
            this.cameraPos = state.cameraPos;
            this.environment = state.environment;
            this.width = state.width();
            this.height = state.height();
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

        public Builder inventory(final Map<String, Integer> newInventory) {
            this.inventory = newInventory;
            return this;
        }

        public Builder playerPos(final PlayerPosition newPos) {
            this.playerPos = newPos;
            return this;
        }

        public Builder cameraPos(final CameraPosition newCameraPos) {
            this.cameraPos = newCameraPos;
            return this;
        }

        public Builder environment(final EnvironmentState newEnvironment) {
            this.environment = newEnvironment;
            return this;
        }

        public Builder width(final int newWidth) {
            this.width = newWidth;
            return this;
        }

        public Builder height(final int newHeight) {
            this.height = newHeight;
            return this;
        }

        public MapState build() {
            return new MapState(
                    version,
                    name,
                    saveName,
                    autosaveName,
                    description,
                    chunks,
                    buildings,
                    playerResources,
                    inventory,
                    playerPos,
                    cameraPos,
                    environment,
                    width,
                    height
            );
        }
    }
}
