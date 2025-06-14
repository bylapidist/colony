package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;

/** Built-in mod providing the default ResourceProductionService factory. */
public final class BaseResourceProductionMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setResourceProductionServiceFactory(s.getResourceProductionServiceFactory());
    }
}
