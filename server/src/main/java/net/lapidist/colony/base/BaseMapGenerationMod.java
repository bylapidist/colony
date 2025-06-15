package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.MapService;

/** Built-in mod providing the default map generator via MapService. */
public final class BaseMapGenerationMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setMapServiceFactory(() -> new MapService(
                s.getMapGenerator(),
                s.getSaveName(),
                s.getMapWidth(),
                s.getMapHeight(),
                s.getStateLock()
        ));
    }
}
