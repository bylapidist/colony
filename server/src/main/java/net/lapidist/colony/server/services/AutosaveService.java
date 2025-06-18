package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.events.autosave.AutosaveEvent;
import net.lapidist.colony.events.autosave.AutosaveStartEvent;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.mostlyoriginal.api.event.common.Event;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.ScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles periodic and shutdown saving of the game state.
 */
public final class AutosaveService extends ScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutosaveService.class);

    private final String saveName;
    private final Supplier<MapState> supplier;
    private final ReentrantLock lock;
    private final Supplier<java.util.List<ModMetadata>> modsSupplier;

    public AutosaveService(
            final long intervalToUse,
            final String saveNameToUse,
            final Supplier<MapState> stateSupplier,
            final Supplier<java.util.List<ModMetadata>> modsSupplierToUse,
            final ReentrantLock lockToUse
    ) {
        super(intervalToUse);
        this.saveName = saveNameToUse;
        this.supplier = stateSupplier;
        this.modsSupplier = modsSupplierToUse;
        this.lock = lockToUse;
    }


    /**
     * Stops the scheduler and saves the game state one final time.
     */
    @Override
    public void stop() {
        super.stop();
        saveOnShutdown();
    }

    @Override
    protected void runTask() {
        saveGameState(AutosaveEvent::new, "Autosaved game state to {} ({} bytes)");
    }

    private void saveOnShutdown() {
        saveGameState(ShutdownSaveEvent::new, "Saved game state to {} ({} bytes) on shutdown");
    }

    private void saveGameState(final java.util.function.BiFunction<Path, Long, Event> creator, final String log) {
        MapState mapState;
        lock.lock();
        try {
            mapState = supplier.get();
        } finally {
            lock.unlock();
        }
        try {
            Path file = Paths.get().getAutosave(saveName);
            Events.dispatch(new AutosaveStartEvent(file));
            Events.update();
            GameStateIO.save(mapState, modsSupplier.get(), file);
            long size = Files.size(file);
            Events.dispatch(creator.apply(file, size));
            Events.update();
            LOGGER.info(log, file, size);
        } catch (IOException e) {
            LOGGER.error("Failed to save game state", e);
        }
    }
}
