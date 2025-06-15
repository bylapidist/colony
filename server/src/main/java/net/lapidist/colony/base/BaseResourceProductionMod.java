package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.ResourceProductionService;

/** Built-in mod providing the default ResourceProductionService factory. */
public final class BaseResourceProductionMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setResourceProductionServiceFactory(() -> new ResourceProductionService(
                s.getAutosaveInterval(),
                s::getMapState,
                s::setMapState,
                s.getNetworkService(),
                s.getStateLock()
        ));
    }

    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.registerSystem(s.getResourceProductionService());
    }
}
