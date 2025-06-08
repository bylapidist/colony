package net.lapidist.colony.network;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Basic network endpoint supporting send and receive operations.
 */
public interface MessageEndpoint {
    void start() throws IOException, InterruptedException;
    void stop();
    void send(Object message);
    <T> void onMessage(Class<T> type, Consumer<T> handler);
}
