package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.CommandHandler;
import net.lapidist.colony.server.commands.ServerCommand;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommandBusTest {

    private static final class DummyCommand implements ServerCommand {
    }

    private static final class DummyHandler implements CommandHandler<DummyCommand> {
        private boolean handled;

        boolean isHandled() {
            return handled;
        }

        @Override
        public Class<DummyCommand> type() {
            return DummyCommand.class;
        }

        @Override
        public void handle(final DummyCommand command) {
            handled = true;
        }
    }

    @Test
    public void dispatchCallsRegisteredHandler() {
        CommandBus bus = new CommandBus();
        DummyHandler handler = new DummyHandler();
        handler.register(bus);

        bus.dispatch(new DummyCommand());

        assertTrue(handler.isHandled());
    }

    private static final class UnhandledCommand implements ServerCommand {
    }

    @Test
    public void dispatchWithoutHandlerDoesNothing() {
        CommandBus bus = new CommandBus();
        bus.dispatch(new UnhandledCommand());
        // Should not throw any exceptions
    }
}
