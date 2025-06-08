package net.lapidist.colony.server.chat;

import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;

/**
 * Chat command that toggles a tile selection.
 */
public final class SelectChatCommand implements ChatCommand {
    private static final int PARTS = 4;
    private static final int ARG_X = 1;
    private static final int ARG_Y = 2;
    private static final int ARG_SELECTED = 3;

    @Override
    public String name() {
        return "select";
    }

    @Override
    public void execute(final String[] args, final CommandBus bus) {
        if (args.length != PARTS) {
            return;
        }
        try {
            int x = Integer.parseInt(args[ARG_X]);
            int y = Integer.parseInt(args[ARG_Y]);
            boolean selected = Boolean.parseBoolean(args[ARG_SELECTED]);
            bus.dispatch(new TileSelectionCommand(x, y, selected));
        } catch (NumberFormatException ignore) {
            // ignore malformed args
        }
    }
}
