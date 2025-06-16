package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.SeasonCycleService;

/** Built-in mod registering the season cycle service. */
public final class BaseSeasonCycleMod implements GameMod {
    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new SeasonCycleService(
                1000,
                60f,
                s::getMapState,
                s::setMapState,
                s.getStateLock()
        ));
    }
}
