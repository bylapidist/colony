package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.commands.CommandBus;

/** Built-in mod providing the default CommandBus factory. */
public final class BaseCommandBusMod implements GameMod {
    @Override
    public void registerServices(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        s.setCommandBusFactory(CommandBus::new);
    }
}
