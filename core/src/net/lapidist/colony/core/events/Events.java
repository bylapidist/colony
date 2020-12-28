package net.lapidist.colony.core.events;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;

public final class Events {

    private Events() {
    }

    public static final int GAME_INIT = 0;
    public static final int PAUSE = 1;
    public static final int RESUME = 2;
    public static final int RESIZE = 3;
    public static final int HIDE = 4;
    public static final int SHOW = 5;

    public static void dispose() {
        MessageManager.getInstance().clear();
        MessageManager.getInstance().clearListeners();
        MessageManager.getInstance().clearProviders();
        MessageManager.getInstance().clearQueue();
    }

    public static void update() {
        MessageManager.getInstance().update();
    }

    public static void enableDebug() {
        MessageManager.getInstance().setDebugEnabled(true);
    }

    public static void dispatch(
            final float delay,
            final int message
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                message
        );
    }

    public static void dispatch(
            final float delay,
            final Telegraph sender,
            final int message
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                sender,
                message
        );
    }

    public void dispatch(
            final float delay,
            final Telegraph sender,
            final Telegraph receiver,
            final int message
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                sender,
                receiver,
                message
        );
    }

    public void dispatch(
            final float delay,
            final Telegraph sender,
            final Telegraph receiver,
            final int message,
            final boolean needsReturnReceipt
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                sender,
                receiver,
                message,
                needsReturnReceipt
        );
    }

    public void dispatch(
            final float delay,
            final Telegraph sender,
            final Telegraph receiver,
            final int message,
            final Object extraInfo
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                sender,
                receiver,
                message,
                extraInfo
        );
    }

    public void dispatch(
            final float delay,
            final Telegraph sender,
            final Telegraph receiver,
            final int message,
            final Object extraInfo,
            final boolean needsReturnReceipt
    ) {
        MessageManager.getInstance().dispatchMessage(
                delay,
                sender,
                receiver,
                message,
                extraInfo,
                needsReturnReceipt
        );
    }
}
