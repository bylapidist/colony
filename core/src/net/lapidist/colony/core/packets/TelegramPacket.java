package net.lapidist.colony.core.packets;

import com.badlogic.gdx.ai.msg.Telegram;

public class TelegramPacket extends AbstractPacket {

    private int message;

    private Object extraInfo;

    public TelegramPacket(final Telegram msg) {
        this.message = msg.message;
        this.extraInfo = msg.extraInfo;
    }

    public final int getMessage() {
        return message;
    }

    public final Object getExtraInfo() {
        return extraInfo;
    }
}
