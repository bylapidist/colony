package net.lapidist.colony.core.codecs.encoders;

import io.netty.buffer.ByteBuf;

public abstract class AbstractPacketEncoder {

    private ByteBuf packet;

    public ByteBuf getPacket() {
        return packet;
    }

    public void setPacket(ByteBuf packet) {
        this.packet = packet;
    }

    protected abstract ByteBuf encode(Object obj);
}
