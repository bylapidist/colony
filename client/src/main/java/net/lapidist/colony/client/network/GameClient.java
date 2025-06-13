package net.lapidist.colony.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.ChunkPos;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.DispatchListener;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.client.network.handlers.MapMetadataHandler;
import net.lapidist.colony.client.network.handlers.MapChunkHandler;
import net.lapidist.colony.client.network.handlers.QueueingMessageHandler;
import net.lapidist.colony.client.network.handlers.ResourceUpdateHandler;
import net.lapidist.colony.components.state.MapChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;


public final class GameClient extends AbstractMessageEndpoint {
    // Increase buffers to handle large serialized map data.
    // Matches the server allocation of 4MB to allow receiving big map states.
    private static final int BUFFER_SIZE = 4 * 1024 * 1024;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private final Client client = new Client(BUFFER_SIZE, BUFFER_SIZE);
    private MapState mapState;
    private final Map<Class<?>, Queue<?>> messageQueues = new ConcurrentHashMap<>();
    private Iterable<MessageHandler<?>> handlers;
    private static final int CONNECT_TIMEOUT = 5000;
    private Consumer<MapState> readyCallback;
    private int playerId = -1;
    private MapState.Builder mapBuilder;
    private java.util.Map<TilePos, TileData> tileBuffer;
    private int expectedChunks;
    private int receivedChunks;
    private java.util.function.Consumer<Float> loadProgressListener;

    private <T> Queue<T> registerQueue(final Class<T> type) {
        Queue<T> queue = new ConcurrentLinkedQueue<>();
        messageQueues.put(type, queue);
        return queue;
    }

    /**
     * Register a listener that receives map loading progress between 0 and 1.
     */
    public void setLoadProgressListener(final java.util.function.Consumer<Float> listener) {
        this.loadProgressListener = listener;
    }

    public GameClient() {
        Queue<TileSelectionData> tileUpdates = registerQueue(TileSelectionData.class);
        Queue<BuildingData> buildingUpdates = registerQueue(BuildingData.class);
        Queue<BuildingRemovalData> buildingRemovals = registerQueue(BuildingRemovalData.class);
        Queue<ChatMessage> chatMessages = registerQueue(ChatMessage.class);
        Queue<ResourceUpdateData> resourceUpdates = registerQueue(ResourceUpdateData.class);

        this.handlers = java.util.List.of(
                new MapMetadataHandler(meta -> {
                    mapBuilder = MapState.builder()
                            .version(meta.version())
                            .name(meta.name())
                            .saveName(meta.saveName())
                            .autosaveName(meta.autosaveName())
                            .description(meta.description())
                            .buildings(new java.util.ArrayList<>(meta.buildings()))
                            .playerResources(meta.playerResources());
                    tileBuffer = new java.util.HashMap<>();
                    expectedChunks = meta.chunkCount();
                    receivedChunks = 0;
                    if (loadProgressListener != null) {
                        loadProgressListener.accept(0f);
                    }
                    if (expectedChunks == 0) {
                        mapState = mapBuilder.chunks(buildChunks(tileBuffer)).build();
                        if (readyCallback != null) {
                            readyCallback.accept(mapState);
                        }
                        if (loadProgressListener != null) {
                            loadProgressListener.accept(1f);
                        }
                    }
                }),
                new MapChunkHandler(this::handleChunk),
                new QueueingMessageHandler<>(TileSelectionData.class, messageQueues),
                new QueueingMessageHandler<>(BuildingData.class, messageQueues),
                new QueueingMessageHandler<>(BuildingRemovalData.class, messageQueues),
                new QueueingMessageHandler<>(ChatMessage.class, messageQueues),
                new ResourceUpdateHandler(messageQueues, () -> mapState, ms -> mapState = ms)
        );
    }

    public GameClient(final Iterable<MessageHandler<?>> handlersToUse) {
        this.handlers = handlersToUse;
    }

    private void connect() throws IOException {
        KryoRegistry.register(client.getKryo());
        client.start();
        LOGGER.info("Connecting to server...");

        registerHandlers(handlers);

        client.addListener(new DispatchListener(this::dispatch) {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connected to server");
                playerId = connection.getID();
            }
        });
        client.connect(CONNECT_TIMEOUT, "localhost", GameServer.TCP_PORT, GameServer.UDP_PORT);
    }

    @Override
    public void start() throws IOException {
        start(ms -> {
        });
    }

    public void start(final Consumer<MapState> callback) throws IOException {
        this.readyCallback = callback;
        connect();
    }

    public MapState getMapState() {
        return mapState;
    }

    public int getPlayerId() {
        return playerId;
    }

    private static java.util.Map<ChunkPos, MapChunkData> buildChunks(final java.util.Map<TilePos, TileData> tiles) {
        java.util.Map<ChunkPos, MapChunkData> chunks = new java.util.HashMap<>();
        for (var entry : tiles.entrySet()) {
            TilePos pos = entry.getKey();
            TileData data = entry.getValue();
            int chunkX = Math.floorDiv(pos.x(), MapChunkData.CHUNK_SIZE);
            int chunkY = Math.floorDiv(pos.y(), MapChunkData.CHUNK_SIZE);
            int localX = Math.floorMod(pos.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(pos.y(), MapChunkData.CHUNK_SIZE);
            ChunkPos posKey = new ChunkPos(chunkX, chunkY);
            MapChunkData chunk = chunks.computeIfAbsent(posKey, p -> new MapChunkData(chunkX, chunkY));
            chunk.getTiles().put(new TilePos(localX, localY), data);
        }
        return chunks;
    }

    private void handleChunk(final MapChunk chunk) {
        if (tileBuffer != null) {
            tileBuffer.putAll(chunk.tiles());
            receivedChunks++;
            if (loadProgressListener != null && expectedChunks > 0) {
                loadProgressListener.accept(receivedChunks / (float) expectedChunks);
            }
            if (receivedChunks >= expectedChunks) {
                mapState = mapBuilder.chunks(buildChunks(tileBuffer)).build();
                if (readyCallback != null) {
                    readyCallback.accept(mapState);
                }
                if (loadProgressListener != null) {
                    loadProgressListener.accept(1f);
                }
                tileBuffer = null;
                mapBuilder = null;
            }
            return;
        }
        if (mapState == null) {
            return;
        }
        ChunkPos posKey = new ChunkPos(chunk.chunkX(), chunk.chunkY());
        MapChunkData data = mapState.chunks().get(posKey);
        if (data == null) {
            data = new MapChunkData(chunk.chunkX(), chunk.chunkY());
            mapState.chunks().put(posKey, data);
        }
        for (var entry : chunk.tiles().entrySet()) {
            TileData td = entry.getValue();
            int localX = Math.floorMod(td.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(td.y(), MapChunkData.CHUNK_SIZE);
            data.getTiles().put(new TilePos(localX, localY), td);
        }
    }

    /**
     * Returns true if the underlying network client is connected to the server.
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    @SuppressWarnings("unchecked")
    public <T> T poll(final Class<T> type) {
        Queue<T> queue = (Queue<T>) messageQueues.get(type);
        return queue != null ? queue.poll() : null;
    }

    @SuppressWarnings("unchecked")
    public void injectTileSelectionUpdate(final TileSelectionData data) {
        ((Queue<TileSelectionData>) messageQueues.get(TileSelectionData.class)).add(data);
    }

    @SuppressWarnings("unchecked")
    public void injectBuildingUpdate(final BuildingData data) {
        ((Queue<BuildingData>) messageQueues.get(BuildingData.class)).add(data);
    }

    @SuppressWarnings("unchecked")
    public void injectBuildingRemoval(final BuildingRemovalData data) {
        ((Queue<BuildingRemovalData>) messageQueues.get(BuildingRemovalData.class)).add(data);
    }

    @SuppressWarnings("unchecked")
    public void injectResourceUpdate(final ResourceUpdateData data) {
        ((Queue<ResourceUpdateData>) messageQueues.get(ResourceUpdateData.class)).add(data);
    }


    public void sendTileSelectionRequest(final TileSelectionData data) {
        send(data);
    }

    public void sendBuildRequest(final BuildingPlacementData data) {
        send(data);
    }

    public void sendRemoveBuildingRequest(final BuildingRemovalData data) {
        send(data);
    }

    public void sendGatherRequest(final ResourceGatherRequestData data) {
        send(data);
    }

    public void sendChatMessage(final ChatMessage message) {
        send(message);
    }

    @Override
    public void send(final Object message) {
        client.sendTCP(message);
    }


    @Override
    public void stop() {
        client.stop();
        LOGGER.info("Client stopped");
    }
}
