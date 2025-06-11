package net.lapidist.colony.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.function.Consumer;

/**
 * Listener that forwards received objects to a dispatcher.
 */
public class DispatchListener extends Listener {

    private final Consumer<Object> dispatcher;

    public DispatchListener(final Consumer<Object> dispatcherToUse) {
        this.dispatcher = dispatcherToUse;
    }

    @Override
    public final void received(final Connection connection, final Object object) {
        dispatcher.accept(object);
    }
}
