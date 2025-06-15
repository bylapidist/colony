package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.EnvironmentUpdate;
import net.lapidist.colony.components.state.MapState;

/** Applies environment updates received from the server. */
public final class EnvironmentUpdateSystem extends BaseSystem {
    private final GameClient client;

    public EnvironmentUpdateSystem(final GameClient clientToUse) {
        this.client = clientToUse;
    }

    @Override
    protected void processSystem() {
        EnvironmentUpdate update;
        while ((update = client.poll(EnvironmentUpdate.class)) != null) {
            MapState state = client.getMapState();
            if (state != null) {
                client.setMapState(state.toBuilder()
                        .environment(update.state())
                        .build());
            }
        }
    }
}
