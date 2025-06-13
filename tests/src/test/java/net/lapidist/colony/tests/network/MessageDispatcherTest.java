package net.lapidist.colony.tests.network;

import net.lapidist.colony.network.MessageDispatcher;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class MessageDispatcherTest {

    private static class BaseMessage {
    }

    private static final class DerivedMessage extends BaseMessage {
    }

    private static final class RecordingHandler<T> implements Consumer<T> {
        private boolean handled;

        @Override
        public void accept(final T t) {
            handled = true;
        }

        boolean handled() {
            return handled;
        }
    }

    @Test
    public void dispatchesExactType() {
        MessageDispatcher dispatcher = new MessageDispatcher();
        RecordingHandler<BaseMessage> handler = new RecordingHandler<>();
        dispatcher.register(BaseMessage.class, handler);

        dispatcher.dispatch(new BaseMessage());

        assertTrue(handler.handled());
    }

    @Test
    public void dispatchesSubclassMessage() {
        MessageDispatcher dispatcher = new MessageDispatcher();
        RecordingHandler<BaseMessage> handler = new RecordingHandler<>();
        dispatcher.register(BaseMessage.class, handler);

        dispatcher.dispatch(new DerivedMessage());

        assertTrue(handler.handled());
    }
}
