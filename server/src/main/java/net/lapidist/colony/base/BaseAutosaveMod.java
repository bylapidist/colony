package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;

/** Built-in mod providing the default AutosaveService factory. */
public final class BaseAutosaveMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setAutosaveServiceFactory(s.getAutosaveServiceFactory());
    }
}
