package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.mostlyoriginal.api.event.common.Event;
import net.lapidist.colony.server.io.GameStateIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Handles periodic and shutdown saving of the game state.
 */
public final class AutosaveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutosaveService.class);

    private final long interval;
    private final String saveName;
    private final Supplier<MapState> supplier;
    private ScheduledExecutorService executor;

    public AutosaveService(
            final long intervalToUse,
            final String saveNameToUse,
            final Supplier<MapState> stateSupplier
    ) {
        this.interval = intervalToUse;
        this.saveName = saveNameToUse;
        this.supplier = stateSupplier;
    }

    /**
     * Starts periodic autosaving on a daemon thread.
     */
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(this::autoSave, interval, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the scheduler and saves the game state one final time.
     */
    public void stop() {
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

    private void saveGameState(final java.util.function.BiFunction<Path, Long, Event> creator, final String log) {
        MapState mapState = supplier.get();
        try {
            Path file = Paths.get().getAutosave(saveName);
            GameStateIO.save(mapState, file);
            long size = Files.size(file);
            Events.dispatch(creator.apply(file, size));
            Events.update();
            LOGGER.info(log, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
