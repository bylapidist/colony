package net.lapidist.colony.tests.network;

import net.lapidist.colony.network.MessageDispatcher;
import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class MessageDispatcherTest {

    @Test
    public void dispatchCallsRegisteredHandler() {
        MessageDispatcher dispatcher = new MessageDispatcher();
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);
        dispatcher.register(String.class, handler);

        dispatcher.dispatch("hello");

        verify(handler).accept("hello");
    }

    @Test
    public void dispatchWithoutHandlerDoesNothing() {
        MessageDispatcher dispatcher = new MessageDispatcher();
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);

        dispatcher.dispatch("hello");

        verifyNoInteractions(handler);
    }
}
