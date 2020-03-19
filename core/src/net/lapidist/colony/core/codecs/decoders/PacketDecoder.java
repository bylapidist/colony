package net.lapidist.colony.core.codecs.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.lapidist.colony.core.packets.TelegramPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected final void decode(
            final ChannelHandlerContext ctx,
            final DatagramPacket msg,
            final List<Object> out
    ) throws Exception {
        byte[] bytes = readBytesFromBuffer(msg.content());
        System.out.println("Received packet, length: " + bytes.length);
        Object payload = deserialize(bytes);

        if (payload instanceof TelegramPacket) {
            TelegramPacket packet = (TelegramPacket) payload;
            System.out.println(packet.getExtraInfo());
            out.add(packet);
        } else {
            System.out.println("Invalid message reached server: " + payload);
        }
    }

    private byte[] readBytesFromBuffer(final ByteBuf in) {
        int readableBytes = in.readableBytes();
        byte[] bytes = new byte[readableBytes];
        in.readBytes(bytes);
        return bytes;
    }

    private Object deserialize(
            final byte[] data
    ) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
