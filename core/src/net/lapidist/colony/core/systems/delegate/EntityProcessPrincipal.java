package net.lapidist.colony.core.systems.delegate;

public interface EntityProcessPrincipal {

    void registerAgent(int entityId, EntityProcessAgent agent);

    void unregisterAgent(int entityId, EntityProcessAgent agent);
}
