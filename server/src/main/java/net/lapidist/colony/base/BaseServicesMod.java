package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;

/** Built-in mod providing default service factories. */
public final class BaseServicesMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setMapServiceFactory(s.getMapServiceFactory());
        s.setNetworkServiceFactory(s.getNetworkServiceFactory());
        s.setAutosaveServiceFactory(s.getAutosaveServiceFactory());
        s.setResourceProductionServiceFactory(s.getResourceProductionServiceFactory());
        s.setCommandBusFactory(s.getCommandBusFactory());
    }
}
