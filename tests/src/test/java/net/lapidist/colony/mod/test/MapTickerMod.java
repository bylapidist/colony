package net.lapidist.colony.mod.test;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.mod.GameSystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** Mod supplying a system that updates map description each tick. */
public final class MapTickerMod implements GameMod {
    public static final AtomicInteger TICKS = new AtomicInteger();

    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new DescriptionTickSystem(s));
    }

    private static final class DescriptionTickSystem implements GameSystem {
        private final net.lapidist.colony.server.GameServer server;
        private ScheduledExecutorService executor;
        private static final int PERIOD_MS = 10;

        DescriptionTickSystem(final net.lapidist.colony.server.GameServer srv) {
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
                    .description("tick" + TICKS.incrementAndGet())
                    .build());
        }
    }
}
