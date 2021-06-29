package net.lapidist.colony.core.events;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;

public final class Events {

    private Events() {
    }

    public enum EventType {
        GAME_INIT(0, "Game Init"),
        PAUSE(1, "Pause"),
        RESUME(2, "Resume"),
        RESIZE(3, "Resize"),
        HIDE(4, "Hide"),
        SHOW(5, "Show");

        private final Telegram telegram;

        EventType(final int messageToSet, final String extraInfoToSet) {
            this.telegram = new Telegram();
            this.telegram.message = messageToSet;
            this.telegram.extraInfo = extraInfoToSet;
        }

        public int getMessage() {
            return this.telegram.message;
        }

        @Override
        public String toString() {
            return (String) this.telegram.extraInfo;
        }
    }

    public static MessageManager getInstance() {
        return MessageManager.getInstance();
    }

    public static void dispose() {
        getInstance().clear();
        getInstance().clearListeners();
        getInstance().clearProviders();
        getInstance().clearQueue();
    }

    public static void update() {
        getInstance().update();
    }

    public static void enableDebug() {
        getInstance().setDebugEnabled(true);
    }

    public static void dispatch(
            final float delay,
            final EventType event
    ) {
        getInstance().dispatchMessage(
                delay,
                event.getMessage(),
                event.toString()
        );
    }
}
