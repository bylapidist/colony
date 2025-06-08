package net.lapidist.colony.server.services;

import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.SaveEvent;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.components.state.MapState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Manages periodic autosaving of the game state.
 */
public final class AutosaveService implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutosaveService.class);

    private final long interval;
    private final String saveName;
    private final Supplier<MapState> stateSupplier;
    private ScheduledExecutorService executor;

    public AutosaveService(
            final long autosaveInterval,
            final String saveNameToUse,
            final Supplier<MapState> stateSupplierToUse
    ) {
        this.interval = autosaveInterval;
        this.saveName = saveNameToUse;
        this.stateSupplier = stateSupplierToUse;
    }

    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(this::autoSave, interval, interval, TimeUnit.MILLISECONDS);
    }

    public void shutdownNow() {
        if (executor != null) {
            executor.shutdownNow();
        }
        saveOnShutdown();
    }

    private void autoSave() {
        saveGameState(AutosaveEvent::new, "Autosaved game state to {} ({} bytes)");
    }

    private void saveOnShutdown() {
        saveGameState(ShutdownSaveEvent::new, "Saved game state to {} ({} bytes) on shutdown");
    }

    private void saveGameState(final BiFunction<Path, Long, SaveEvent> creator, final String log) {
        try {
            MapState state = stateSupplier.get();
            Path file = Paths.getAutosave(saveName);
            GameStateIO.save(state, file);
            long size = Files.size(file);
            Events.dispatch(creator.apply(file, size));
            Events.update();
            LOGGER.info(log, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        shutdownNow();
    }
}
