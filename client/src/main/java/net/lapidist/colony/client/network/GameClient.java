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
import net.lapidist.colony.components.state.MapChunkRequest;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.serialization.KryoRegistry;
import net.lapidist.colony.network.AbstractMessageEndpoint;
import net.lapidist.colony.network.DispatchListener;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.config.NetworkConfig;
import net.lapidist.colony.client.network.handlers.MapMetadataHandler;
import net.lapidist.colony.client.network.handlers.MapChunkHandler;
import net.lapidist.colony.client.network.handlers.QueueingMessageHandler;
import net.lapidist.colony.client.network.handlers.ResourceUpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class GameClient extends AbstractMessageEndpoint {
    // Increase buffers to handle large serialized map data.
    // Size is configured via game.networkBufferSize.
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private final Client client = new Client(GameConstants.NETWORK_BUFFER_SIZE, GameConstants.NETWORK_BUFFER_SIZE);
    private MapState mapState;
    private final Map<Class<?>, Queue<?>> messageQueues = new ConcurrentHashMap<>();
    private Iterable<MessageHandler<?>> handlers;
    private static final int CONNECT_TIMEOUT = 5000;
    private Consumer<MapState> readyCallback;
    private int playerId = -1;
    public static final int CHUNK_REQUEST_BATCH_SIZE = 8;
    private static final int CHUNK_REQUEST_INTERVAL_MS = 16;
    private final Deque<ChunkPos> pendingChunkRequests = new ConcurrentLinkedDeque<>();
    private ScheduledExecutorService requestExecutor;
    private MapState.Builder mapBuilder;
    private java.util.Map<TilePos, TileData> tileBuffer;
    private int expectedChunks;
    private int receivedChunks;
    private int mapWidth = MapState.DEFAULT_WIDTH;
    private int mapHeight = MapState.DEFAULT_HEIGHT;
    private java.util.function.Consumer<Float> loadProgressListener;
    private java.util.function.Consumer<String> loadMessageListener;

    private <T> Queue<T> registerQueue(final Class<T> type) {
        Queue<T> queue = new ConcurrentLinkedQueue<>();
        messageQueues.put(type, queue);
        return queue;
    }

    private void startRequestExecutor() {
        if (requestExecutor != null && !requestExecutor.isShutdown()) {
            return;
        }
        requestExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "chunk-requests");
            t.setDaemon(true);
            return t;
        });
        requestExecutor.scheduleAtFixedRate(() -> {
            if (tileBuffer != null) {
                processChunkRequestQueue();
            }
        }, 0, CHUNK_REQUEST_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    private void stopRequestExecutor() {
        if (requestExecutor != null) {
            requestExecutor.shutdownNow();
            requestExecutor = null;
        }
    }

    /**
     * Register a listener that receives map loading progress between 0 and 1.
     */
    public void setLoadProgressListener(final java.util.function.Consumer<Float> listener) {
        this.loadProgressListener = listener;
    }

    /**
     * Register a listener that receives textual loading status updates.
     */
    public void setLoadMessageListener(final java.util.function.Consumer<String> listener) {
        this.loadMessageListener = listener;
    }

    public GameClient() {
        Queue<TileSelectionData> tileUpdates = registerQueue(TileSelectionData.class);
        Queue<BuildingData> buildingUpdates = registerQueue(BuildingData.class);
        Queue<BuildingRemovalData> buildingRemovals = registerQueue(BuildingRemovalData.class);
        Queue<ChatMessage> chatMessages = registerQueue(ChatMessage.class);
        Queue<ResourceUpdateData> resourceUpdates = registerQueue(ResourceUpdateData.class);

        this.handlers = java.util.List.of(
                new MapMetadataHandler(meta -> {
                    if (loadMessageListener != null) {
                        loadMessageListener.accept(net.lapidist.colony.i18n.I18n.get("loading.metadata"));
                    }
                    mapBuilder = MapState.builder()
                            .version(meta.version())
                            .name(meta.name())
                            .saveName(meta.saveName())
                            .autosaveName(meta.autosaveName())
                            .description(meta.description())
                            .width(meta.width())
                            .height(meta.height())
                            .buildings(new java.util.ArrayList<>(meta.buildings()))
                            .playerResources(meta.playerResources());
                    tileBuffer = new java.util.HashMap<>();
                    expectedChunks = meta.chunkCount();
                    mapWidth = meta.width();
                    mapHeight = meta.height();
                    receivedChunks = 0;
                    if (loadProgressListener != null) {
                        loadProgressListener.accept(0f);
                    }
                    int chunkWidth = (int) Math.ceil(mapWidth / (double) MapChunkData.CHUNK_SIZE);
                    int chunkHeight = (int) Math.ceil(mapHeight / (double) MapChunkData.CHUNK_SIZE);
                    for (int x = 0; x < chunkWidth; x++) {
                        for (int y = 0; y < chunkHeight; y++) {
                            queueChunkRequest(x, y);
                        }
                    }
                    if (loadMessageListener != null) {
                        loadMessageListener.accept(net.lapidist.colony.i18n.I18n.get("loading.chunks"));
                    }
                }),
                new MapChunkHandler(this::handleChunk, client.getKryo()),
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
        if (loadMessageListener != null) {
            loadMessageListener.accept(net.lapidist.colony.i18n.I18n.get("loading.connect"));
        }

        registerHandlers(handlers);

        client.addListener(new DispatchListener(this::dispatch) {
            @Override
            public void connected(final Connection connection) {
                LOGGER.info("Connected to server");
                playerId = connection.getID();
            }
        });
        client.connect(
                CONNECT_TIMEOUT,
                NetworkConfig.getHost(),
                NetworkConfig.getTcpPort(),
                NetworkConfig.getUdpPort()
        );
    }

    @Override
    public void start() throws IOException {
        start(ms -> {
        });
    }

    public void start(final Consumer<MapState> callback) throws IOException {
        this.readyCallback = callback;
        connect();
        startRequestExecutor();
    }

    public MapState getMapState() {
        return mapState;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
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

    private void handleChunk(final MapChunkData chunk) {
        if (tileBuffer != null) {
            for (var entry : chunk.getTiles().entrySet()) {
                TileData td = entry.getValue();
                tileBuffer.put(new TilePos(td.x(), td.y()), td);
            }
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
                // ensure no queued requests remain
                processChunkRequestQueue(Integer.MAX_VALUE);
                pendingChunkRequests.clear();
                // Switch to incremental chunk handling after the initial load
                tileBuffer = null;
                mapBuilder = null;
                readyCallback = null;
                stopRequestExecutor();
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
        for (var entry : chunk.getTiles().entrySet()) {
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

    /**
     * Queue a chunk request to be processed later.
     */
    public void queueChunkRequest(final int chunkX, final int chunkY) {
        pendingChunkRequests.add(new ChunkPos(chunkX, chunkY));
    }

    /**
     * Send up to {@code maxRequests} queued chunk requests.
     */
    public void processChunkRequestQueue(final int maxRequests) {
        for (int i = 0; i < maxRequests; i++) {
            ChunkPos pos = pendingChunkRequests.poll();
            if (pos == null) {
                break;
            }
            send(new MapChunkRequest(pos.x(), pos.y()));
        }
    }

    /**
     * Convenience overload using {@link #CHUNK_REQUEST_BATCH_SIZE} as the limit.
     */
    public void processChunkRequestQueue() {
        processChunkRequestQueue(CHUNK_REQUEST_BATCH_SIZE);
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
        stopRequestExecutor();
        client.stop();
        LOGGER.info("Client stopped");
    }
}
