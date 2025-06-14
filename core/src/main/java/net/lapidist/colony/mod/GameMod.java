package net.lapidist.colony.mod;

/**
 * Extension point for additional game functionality.
 */
public interface GameMod {

    /**
     * Called after the mod has been loaded.
     */
    default void init() {
    }

    /**
     * Called when the mod is unloaded.
     */
    default void dispose() {
    }

    /**
     * Register additional server services.
     *
     * <p>This hook runs during server start up after the core services are created
     * but before the server begins accepting connections.</p>
     *
     * @param server active server instance
     */
    default void registerServices(GameServer server) {
    }

    /**
     * Register custom command handlers with the game server.
     *
     * <p>Invoked during startup after default handlers are registered. Mods may use this method to
     * add their own command handlers via the provided {@link CommandBus}.</p>
     *
     * @param bus command bus to register handlers with
     */
    default void registerHandlers(CommandBus bus) {
    }
}
