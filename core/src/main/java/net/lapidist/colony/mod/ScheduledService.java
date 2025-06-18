package net.lapidist.colony.mod;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base class for services that run periodically on a single daemon thread.
 */
public abstract class ScheduledService implements GameSystem {

    private final long interval;
    private ScheduledExecutorService executor;

    protected ScheduledService(final long intervalMs) {
        this.interval = intervalMs;
    }

    /**
     * Starts a daemon thread that invokes {@link #runTask()} at the configured interval.
     */
    @Override
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::runTask, interval, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the scheduler. Subclasses overriding this method must call {@code super.stop()}.
     */
    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /** Task executed on each interval. */
    protected abstract void runTask();
}
