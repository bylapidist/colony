package net.lapidist.colony.network;

/**
 * Simple chat message exchanged between clients via the server.
 *
 * @param playerId id of the sending player
 * @param text     chat text
 */
public record ChatMessage(int playerId, String text) {

    /**
     * Convenience constructor without a player id. Primarily used by tests.
     *
     * @param message chat text
     */
    public ChatMessage(final String message) {
        this(-1, message);
    }
}
