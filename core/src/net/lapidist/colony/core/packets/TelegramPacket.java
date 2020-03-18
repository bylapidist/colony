package net.lapidist.colony.core.packets;

import com.badlogic.gdx.ai.msg.Telegram;

public class TelegramPacket extends AbstractPacket {

    /** The message type. */
    private int message;

    /** Any additional information that may accompany the message */
    private Object extraInfo;

    public TelegramPacket(Telegram msg) {
        this.message = msg.message;
        this.extraInfo = msg.extraInfo;
    }

    public int getMessage() {
        return message;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }
}
