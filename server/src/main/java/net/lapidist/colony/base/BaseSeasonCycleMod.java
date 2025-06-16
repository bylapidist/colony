package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.SeasonCycleService;

/** Built-in mod registering the season cycle service. */
public final class BaseSeasonCycleMod implements GameMod {
    private static final long TICK_PERIOD_MS = 1000L;
    private static final float SEASON_LENGTH_SECONDS = 60f;
    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new SeasonCycleService(
                TICK_PERIOD_MS,
                SEASON_LENGTH_SECONDS,
                s::getMapState,
                s::setMapState,
                s.getStateLock()
        ));
    }
}
