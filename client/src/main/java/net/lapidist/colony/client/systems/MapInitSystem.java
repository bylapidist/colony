package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.map.MapStateProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Initializes the game world using a {@link MapStateProvider}.
 */
@SuppressWarnings("DesignForExtension")
public class MapInitSystem extends BaseSystem {
    private final MapStateProvider provider;
    private final Consumer<Float> progress;
    private ExecutorService executor;
    private Future<net.lapidist.colony.components.state.MapState> future;
    private static final float HALF = 0.5f;
    private boolean done;
    private float current;

    public MapInitSystem(final MapStateProvider providerToSet) {
        this(providerToSet, null);
    }

    public MapInitSystem(
            final MapStateProvider providerToSet,
            final Consumer<Float> progressCallback
    ) {
        this.provider = providerToSet;
        this.progress = progressCallback;
    }

    @Override
    public final void initialize() {
        if (progress == null) {
            MapFactory.create(world, provider.provide());
            done = true;
            return;
        }
        executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "map-gen");
            t.setDaemon(true);
            return t;
        });
        future = executor.submit(provider::provide);
        current = 0f;
        progress.accept(current);
    }

    @Override
    protected final void processSystem() {
        if (done) {
            return;
        }
        if (future != null && future.isDone()) {
            try {
                var state = future.get();
                progress.accept(HALF);
                MapFactory.create(world, state, p -> {
                    current = HALF + p / 2f;
                    progress.accept(current);
                });
                current = 1f;
                progress.accept(1f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdown();
                done = true;
            }
        }
    }

    /** Process loading task outside the system lifecycle. */
    public void update() {
        processSystem();
    }

    public final boolean isReady() {
        return done;
    }

    public final float getProgress() {
        return current;
    }
}
