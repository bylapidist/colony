package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.CommandHandler;
import net.lapidist.colony.server.commands.ServerCommand;
import net.lapidist.colony.network.AbstractMessageHandler;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class GameServerHandlerRegistrationTest {

    private static final class DummyMessage {
    }

    private static final class DummyMessageHandler extends AbstractMessageHandler<DummyMessage> {
        private boolean handled;

        DummyMessageHandler() {
            super(DummyMessage.class);
        }

        @Override
        public void handle(final DummyMessage message) {
            handled = true;
        }

        boolean handled() {
            return handled;
        }
    }

    private static final class DummyCommand implements ServerCommand {
    }

    private static final class DummyCommandHandler implements CommandHandler<DummyCommand> {
        private boolean handled;
        private CommandBus bus;

        @Override
        public Class<DummyCommand> type() {
            return DummyCommand.class;
        }

        @Override
        public void handle(final DummyCommand command) {
            handled = true;
        }

        @Override
        public void register(final CommandBus b) {
            this.bus = b;
            CommandHandler.super.register(b);
        }

        CommandBus getBus() {
            return bus;
        }

        boolean handled() {
            return handled;
        }
    }

    @Test
    public void registersAndDispatchesCustomHandlers() throws Exception {
        DummyMessageHandler messageHandler = new DummyMessageHandler();
        DummyCommandHandler commandHandler = new DummyCommandHandler();
        GameServer server = new GameServer(
                GameServerConfig.builder().build(),
                java.util.List.of(messageHandler),
                java.util.List.of(commandHandler)
        );
        server.start();

        Method dispatch = net.lapidist.colony.network.AbstractMessageEndpoint.class
                .getDeclaredMethod("dispatch", Object.class);
        dispatch.setAccessible(true);
        dispatch.invoke(server, new DummyMessage());
        commandHandler.getBus().dispatch(new DummyCommand());

        assertTrue(messageHandler.handled());
        assertTrue(commandHandler.handled());

        server.stop();
    }
}
