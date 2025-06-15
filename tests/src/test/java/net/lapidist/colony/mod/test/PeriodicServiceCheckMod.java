package net.lapidist.colony.mod.test;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.GameSystem;
import net.lapidist.colony.components.state.MapState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Mod that verifies system registration occurs after services are created. */
public final class PeriodicServiceCheckMod implements GameMod {
    public static final AtomicInteger TICKS = new AtomicInteger();
    public static final AtomicBoolean SERVICES_AVAILABLE = new AtomicBoolean();

    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        SERVICES_AVAILABLE.set(s.getResourceProductionService() != null
                && s.getNetworkService() != null
                && s.getAutosaveInterval() > 0);
        srv.registerSystem(new PeriodicSystem(s));
    }

    private static final class PeriodicSystem implements GameSystem {
        private final net.lapidist.colony.server.GameServer server;
        private ScheduledExecutorService executor;
        private static final int PERIOD_MS = 10;

        PeriodicSystem(final net.lapidist.colony.server.GameServer srv) {
            this.server = srv;
        }

        @Override
        public void start() {
            executor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
            executor.scheduleAtFixedRate(this::tick, PERIOD_MS, PERIOD_MS, TimeUnit.MILLISECONDS);
        }

        @Override
        public void stop() {
            if (executor != null) {
                executor.shutdownNow();
            }
        }

        private void tick() {
            MapState state = server.getMapState();
            server.setMapState(state.toBuilder()
                    .description("svc" + TICKS.incrementAndGet())
                    .build());
        }
    }
}
