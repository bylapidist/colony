package net.lapidist.colony.mod;

/**
 * Minimal server API exposed to mods. This placeholder avoids a build-time
 * dependency on the server module while still allowing registration of
 * {@link GameSystem} instances.
 */
public interface GameServer {

    /**
     * Register a {@link GameSystem} to start and stop
     * with the server lifecycle.
     *
     * @param system system to manage
     */
    default void registerSystem(GameSystem system) {
        // no-op for non-server implementations
    }

    /**
     * Start the server and all registered services.
     *
     * <p>Client implementations may ignore this call.</p>
     */
    default void start() throws java.io.IOException, InterruptedException {
        // no-op for non-server implementations
    }

    /** Stop the server and release resources. */
    default void stop() {
        // no-op for non-server implementations
    }

    /** Send a message to all connected clients. */
    default void broadcast(Object message) {
        // no-op for non-server implementations
    }

    /** Override the factory for creating the map service. */
    default void setMapServiceFactory(java.util.function.Supplier<?> factory) {
        // no-op for non-server implementations
    }

    /** Override the factory for creating the network service. */
    default void setNetworkServiceFactory(java.util.function.Supplier<?> factory) {
        // no-op for non-server implementations
    }

    /** Override the factory for creating the autosave service. */
    default void setAutosaveServiceFactory(java.util.function.Supplier<?> factory) {
        // no-op for non-server implementations
    }

    /** Override the factory for creating the resource production service. */
    default void setResourceProductionServiceFactory(java.util.function.Supplier<?> factory) {
        // no-op for non-server implementations
    }

    /** Override the factory for creating the command bus. */
    default void setCommandBusFactory(java.util.function.Supplier<?> factory) {
        // no-op for non-server implementations
    }
}
