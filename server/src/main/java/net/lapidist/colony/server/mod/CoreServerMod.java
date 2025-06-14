package net.lapidist.colony.server.mod;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.mod.CommandBus;

import java.lang.reflect.Method;

/** Built-in server mod providing default services and handlers. */
public final class CoreServerMod implements GameMod {
    private net.lapidist.colony.server.GameServer server;

    @Override
    public void registerServices(final GameServer srv) {
        this.server = (net.lapidist.colony.server.GameServer) srv;
        net.lapidist.colony.server.GameServer s = this.server;
        // Default factories are already initialised in GameServer's constructor.
        // Invoking setters ensures mods can override them before services are created.
        s.setMapServiceFactory(s.getMapServiceFactory());
        s.setNetworkServiceFactory(s.getNetworkServiceFactory());
        s.setAutosaveServiceFactory(s.getAutosaveServiceFactory());
        s.setResourceProductionServiceFactory(s.getResourceProductionServiceFactory());
    }

    @Override
    public void registerHandlers(final CommandBus bus) {
        try {
            Method m = net.lapidist.colony.server.GameServer.class.getDeclaredMethod("registerDefaultHandlers");
            m.setAccessible(true);
            m.invoke(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
