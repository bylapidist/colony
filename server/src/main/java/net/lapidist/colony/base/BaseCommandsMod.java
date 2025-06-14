package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.mod.CommandBus;

/** Built-in mod registering default command and message handlers. */
public final class BaseCommandsMod implements GameMod {
    private net.lapidist.colony.server.GameServer server;

    @Override
    public void registerServices(final GameServer srv) {
        this.server = (net.lapidist.colony.server.GameServer) srv;
    }

    @Override
    public void registerHandlers(final CommandBus bus) {
        server.registerDefaultHandlers();
    }
}
