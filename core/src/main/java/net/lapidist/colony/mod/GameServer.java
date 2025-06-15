package net.lapidist.colony.mod;

/**
 * Minimal server API exposed to mods. This placeholder avoids a build-time
 * dependency on the server module while still allowing registration of
 * {@link net.lapidist.colony.server.GameSystem} instances.
 */
public interface GameServer {

    /**
     * Register a {@link net.lapidist.colony.server.GameSystem} to start and stop
     * with the server lifecycle.
     *
     * @param system system to manage
     */
    default void registerSystem(net.lapidist.colony.server.GameSystem system) {
        // no-op for non-server implementations
    }
}
