package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.DayNightCycleService;

/** Built-in mod registering the day/night cycle service. */
public final class BaseDayCycleMod implements GameMod {
    private static final long TICK_PERIOD_MS = 1000L;
    private static final float DAY_LENGTH_SECONDS = 24f;

    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new DayNightCycleService(
                TICK_PERIOD_MS,
                DAY_LENGTH_SECONDS,
                s::getMapState,
                s::setMapState,
                s.getStateLock()
        ));
    }
}
