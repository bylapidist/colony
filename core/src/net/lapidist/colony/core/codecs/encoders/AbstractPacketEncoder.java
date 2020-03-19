package net.lapidist.colony.core.codecs.encoders;

import io.netty.buffer.ByteBuf;

public abstract class AbstractPacketEncoder {

    private ByteBuf packet;

    public final ByteBuf getPacket() {
        return packet;
    }

    public final void setPacket(final ByteBuf packetToSet) {
        this.packet = packetToSet;
    }

    protected abstract ByteBuf encode(Object obj);
}
