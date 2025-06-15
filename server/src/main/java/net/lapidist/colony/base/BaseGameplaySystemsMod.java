package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.ResourceProductionService;

/** Built-in mod registering default gameplay systems. */
public final class BaseGameplaySystemsMod implements GameMod {
    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        ResourceProductionService service = s.getResourceProductionService();
        if (service != null) {
            srv.registerSystem(service);
        }
        srv.registerSystem(new net.lapidist.colony.server.systems.EnvironmentSystem(s));
    }
}
