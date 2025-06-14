package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;

/** Built-in mod providing the default NetworkService factory. */
public final class BaseNetworkMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setNetworkServiceFactory(s.getNetworkServiceFactory());
    }
}
