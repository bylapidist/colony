package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.map.MapStateProvider;
import net.lapidist.colony.components.state.map.MapState;

import java.util.function.Consumer;

/**
 * Initializes the game world using a {@link MapStateProvider}.
 */
public class MapInitSystem extends BaseSystem {
    private final MapStateProvider provider;
    private final boolean async;
    private final Consumer<Float> progressCallback;
    private volatile boolean ready;
    private MapState pendingState;
    private Thread worker;

    public MapInitSystem(final MapStateProvider providerToSet) {
        this(providerToSet, false, null);
    }

    public MapInitSystem(
            final MapStateProvider providerToSet,
            final boolean asyncFlag,
            final Consumer<Float> callback
    ) {
        this.provider = providerToSet;
        this.async = asyncFlag;
        this.progressCallback = callback;
    }

    @Override
    public final void initialize() {
        ready = false;
        if (async) {
            worker = new Thread(() -> {
                if (progressCallback != null) {
                    progressCallback.accept(0f);
                }
                pendingState = provider.provide();
                if (progressCallback != null) {
                    progressCallback.accept(1f);
                }
            }, "map-init");
            worker.setDaemon(true);
            worker.start();
        } else {
            MapFactory.create(world, provider.provide());
            ready = true;
        }
    }

    @Override
    protected final void processSystem() {
        if (async && pendingState != null) {
            MapFactory.create(world, pendingState);
            pendingState = null;
            ready = true;
        }
        // initialization occurs once
    }

    /** Indicates whether the map has been created. */
    public boolean isReady() {
        return ready;
    }

    @Override
    public final void dispose() {
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
            try {
                worker.join();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
